package am.innline.book.search.presentation

import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.usecase.book.GetSearchBooksUseCase
import am.innline.book.search.domain.usecase.search_history.ClearSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.DeleteSearchQueryUseCase
import am.innline.book.search.domain.usecase.search_history.GetSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.InsertSearchQueryUseCase
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchBooksUseCase: GetSearchBooksUseCase,
    private val searchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearHistoryUseCase: ClearSearchHistoryUseCase,
    private val deleteQueryUseCase: DeleteSearchQueryUseCase,
    private val insertQueryUseCase: InsertSearchQueryUseCase,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    // Search query with default value
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // UI state
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState = _searchUiState.asStateFlow()

    // Search history
    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    private var _pagingData: Flow<PagingData<Book>>? = null
    val pagingData: Flow<PagingData<Book>>
        get() {
            return _pagingData ?: createPagingFlow().also {
                _pagingData = it
            }
        }

    private var currentSearchTerm = ""

    init {
        loadSearchHistory()
        setupSearchDebounce()

        setSearchQuery("Kotlin")
        triggerSearch()
    }

    private fun createPagingFlow(): Flow<PagingData<Book>> {
        currentSearchTerm = _searchQuery.value
        return searchBooksUseCase(currentSearchTerm)
            .cachedIn(viewModelScope)
            .onStart {
                _searchUiState.value = when (_searchUiState.value) {
                    SearchUiState.Idle -> SearchUiState.InitialLoading
                    else -> SearchUiState.PaginationLoading
                }
            }
            .catch { e ->
                _searchUiState.value = if (isNetworkAvailable()) {
                    SearchUiState.Error(e.message ?: "Unknown error")
                } else {
                    SearchUiState.NoInternet
                }
            }
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun loadSearchHistory() {
        searchHistoryUseCase()
            .map { history -> history.map { it.query } }
            .onEach { _searchHistory.value = it }
            .launchIn(viewModelScope)
    }

    private fun setupSearchDebounce() {
        _searchQuery
            .debounce(500)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun triggerSearch() {
        if (_searchQuery.value.isNotBlank() && _searchQuery.value != currentSearchTerm) {
            viewModelScope.launch {
                insertQueryUseCase(_searchQuery.value)
            }
            _pagingData = null // Only clearing cache if search term changed
            _searchUiState.value = SearchUiState.InitialLoading
        }
    }

    fun deleteSearchItem(query: String) {
        viewModelScope.launch {
            deleteQueryUseCase(query)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            clearHistoryUseCase()
        }
    }

    fun retry() {
        if (_searchQuery.value.isNotBlank()) {
            _pagingData = null
            _searchUiState.value = SearchUiState.InitialLoading
        }
    }
}