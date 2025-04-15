package am.innline.book.favorites.presentation

import am.innline.book.details.domain.repo.DownloadedBooksRepository
import am.innline.book.favorites.data.worker.DownloadBooksWorker
import am.innline.book.favorites.domain.model.FavoriteBook
import am.innline.book.favorites.domain.usecase.AddFavoriteBookUseCase
import am.innline.book.favorites.domain.usecase.FetchBooksFromApiUseCase
import am.innline.book.favorites.domain.usecase.GetFavoriteBooksUseCase
import am.innline.book.favorites.domain.usecase.RemoveFavoriteBookUseCase
import am.innline.book.search.domain.model.Book
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class FavoritesViewModel(
    private val getFavoriteBooksUseCase: GetFavoriteBooksUseCase,
    private val addFavoriteBookUseCase: AddFavoriteBookUseCase,
    private val removeFavoriteBookUseCase: RemoveFavoriteBookUseCase,
    private val fetchBooksFromApiUseCase: FetchBooksFromApiUseCase,
    private val downloadedBooksRepository: DownloadedBooksRepository,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    // State for local favorites
    private val _favoriteIds = MutableStateFlow<List<FavoriteBook>>(emptyList())
    val favoriteIds = _favoriteIds.asStateFlow()

    // Cache for API responses and downloaded books
    private val _booksCache = mutableMapOf<String, Book>()
    private val _favoriteBooks = MutableStateFlow<List<Book>>(emptyList())

    // State for UI
    private val _booksState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Empty)
    val booksState = _booksState.asStateFlow()

    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Idle)
    val downloadState = _downloadState.asStateFlow()

    init {
        loadFavoritesIds()
    }

    private fun loadFavoritesIds() {
        getFavoriteBooksUseCase().onEach { favorites ->
            _favoriteIds.value = favorites
            when {
                favorites.isEmpty() -> {
                    _booksState.value = FavoriteUiState.Empty
                    _favoriteBooks.value = emptyList()
                    _booksCache.clear()
                }

                else -> {
                    // First try to load from downloaded books (Room)
                    loadFromDownloadedBooks(favorites)

                    // Then try to fetch from API if network is available
                    if (isNetworkAvailable() && favorites.any { it.id !in _booksCache }) {
                        fetchBooksFromApi()
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadFromDownloadedBooks(favorites: List<FavoriteBook>) {
        viewModelScope.launch {
            try {
                val downloadedBooks = downloadedBooksRepository.getAllDownloadedBooks().first()
                val downloadedBooksMap = downloadedBooks.associateBy { it.id }

                // Update cache with downloaded books
                downloadedBooksMap.forEach { (id, book) ->
                    _booksCache[id] = book
                }

                updateBooksFromCache(favorites)
            } catch (e: Exception) {
                // If no downloaded books, just proceed
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }

    private fun updateBooksFromCache(favorites: List<FavoriteBook>) {
        val cachedBooks = favorites.mapNotNull { favorite ->
            _booksCache[favorite.id]?.let { book ->
                // If book is from shared prefs (only has title), create a minimal Book object
                if (book.hasOnlyTitle()) {
                    book.copy(title = favorite.title)
                } else {
                    book
                }
            } ?: run {
                // If not in cache, create a minimal Book with just the title
                Book(
                    id = favorite.id,
                    title = favorite.title,
                    authors = emptyList(),
                    description = "",
                    thumbnailUrl = null
                )
            }
        }

        _favoriteBooks.value = cachedBooks
        _booksState.value = if (cachedBooks.isNotEmpty()) {
            FavoriteUiState.Success(cachedBooks)
        } else {
            FavoriteUiState.Empty
        }
    }

    private fun fetchBooksFromApi() {
        val bookIds = _favoriteIds.value.map { it.id }
        if (bookIds.isEmpty()) return

        _booksState.value = FavoriteUiState.Loading

        fetchBooksFromApiUseCase(bookIds).onEach { books ->
            // Update cache
            books.forEach { book -> _booksCache[book.id] = book }

            // Update state from cache (in case some books failed to fetch)
            updateBooksFromCache(_favoriteIds.value)
        }.catch {
            _booksState.value = FavoriteUiState.Error(it.message ?: "Failed to fetch books")
            // Still try to show what we have in cache
            updateBooksFromCache(_favoriteIds.value)
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(book: Book) {
        val currentFavorites = _favoriteIds.value
        val favoriteBook = FavoriteBook(book.id, book.title)

        viewModelScope.launch(Dispatchers.IO) {
            if (currentFavorites.any { it.id == book.id }) {
                removeFavoriteBook(favoriteBook)

                _favoriteIds.update { it.filterNot { fav -> fav.id == book.id } }
                _booksCache.remove(book.id)
                _favoriteBooks.update { it.filterNot { b -> b.id == book.id } }
            } else {
                addFavoriteBook(favoriteBook)

                _favoriteIds.update { it + favoriteBook }
                _booksCache[book.id] = book
                _favoriteBooks.update { it + book }
            }
        }
    }

    private fun <T> MutableStateFlow<T>.update(transform: (T) -> T) {
        this.value = transform(this.value)
    }

    private suspend fun removeFavoriteBook(book: FavoriteBook) {
        try {
            removeFavoriteBookUseCase(book)
        } catch (e: Exception) {
            _favoriteIds.value = _favoriteIds.value.toMutableList().apply { add(book) }
            _booksCache[book.id] = _booksCache[book.id] ?: return
            _favoriteBooks.value = _favoriteBooks.value.toMutableList().apply {
                add(_booksCache[book.id]!!)
            }
        }
    }

    private suspend fun addFavoriteBook(book: FavoriteBook) {
        addFavoriteBookUseCase(book)
    }

    fun downloadAllFavoritesForOffline(context: Context, lifecycleOwner: LifecycleOwner) {
        val booksToDownload = _favoriteBooks.value
        if (booksToDownload.isEmpty()) return

        _downloadState.value = DownloadState.InProgress(0, booksToDownload.size)

        // Create temporary file
        val tempFile =
            File(context.cacheDir, "temp_books_${System.currentTimeMillis()}.json").apply {
                writeText(Gson().toJson(booksToDownload))
            }

        val data = workDataOf(
            "filePath" to tempFile.absolutePath,
            "totalBooks" to booksToDownload.size,
            "syncStrategy" to "optimized" // Flag for optimized sync
        )

        val request = OneTimeWorkRequestBuilder<DownloadBooksWorker>().setInputData(data).build()

        // Remove previous observer if any
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
            .removeObservers(lifecycleOwner) // You'll need to pass lifecycleOwner

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)

            .observeForever { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> {
                        val progress = workInfo.progress.getInt("progress", 0)
                        _downloadState.value = DownloadState.InProgress(
                            current = progress, total = booksToDownload.size
                        )
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        _downloadState.value = DownloadState.Completed
                        // Reset after showing completion
                        viewModelScope.launch {
                            delay(2000) // Show completion for 2 seconds
                            _downloadState.value = DownloadState.Idle
                        }
                    }

                    WorkInfo.State.FAILED -> {
                        _downloadState.value = DownloadState.Error("Download failed")
                        // Reset after showing error
                        viewModelScope.launch {
                            delay(2000)
                            _downloadState.value = DownloadState.Idle
                        }
                    }

                    else -> {
                        Log.e(
                            "DownloadBooksWorker",
                            "downloadAllFavoritesForOffline: workInfo.state = ${workInfo?.state}",
                        )
                    }
                }
            }

        WorkManager.getInstance(context).enqueue(request)
    }
}

fun Book.hasOnlyTitle(): Boolean {
    return this.authors.isEmpty() && this.description.isEmpty() && this.thumbnailUrl.isNullOrEmpty()
}