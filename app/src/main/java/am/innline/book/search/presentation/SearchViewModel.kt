package am.innline.book.search.presentation

import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.usecase.book.GetSearchBooksUseCase
import am.innline.book.search.domain.usecase.search_history.ClearSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.DeleteSearchQueryUseCase
import am.innline.book.search.domain.usecase.search_history.GetSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.InsertSearchQueryUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchBooksUseCase: GetSearchBooksUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val deleteSearchQueryUseCase: DeleteSearchQueryUseCase,
    private val insertSearchQueryUseCase: InsertSearchQueryUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("Kotlin")
    val searchQuery = _searchQuery.asStateFlow()

    private val _showInitialLoader = MutableStateFlow(false)
    val showInitialLoader = _showInitialLoader.asStateFlow()

    private val _showPaginationLoader = MutableStateFlow(false)
    val showPaginationLoader = _showPaginationLoader.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    private val _searchTrigger = MutableStateFlow(0)
    val books: Flow<PagingData<Book>> = _searchTrigger
        .flatMapLatest {
            if (_searchQuery.value.isNotBlank()) {
                getSearchBooksUseCase(_searchQuery.value)
                    .cachedIn(viewModelScope)
                    .onStart { _showInitialLoader.value = true }
                    .onEach { _showInitialLoader.value = false }
            } else {
                flowOf(PagingData.empty())
            }
        }

    init {
        fetchSearchHistory()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun triggerSearch() {
        if (_searchQuery.value.isNotBlank()) {
            viewModelScope.launch {
                insertSearchQuery(_searchQuery.value)
            }
            _searchTrigger.value++
        }
    }

    init {
        fetchSearchHistory()
    }


    fun deleteSearchItem(query: String) {
        viewModelScope.launch {
            deleteSearchQueryUseCase(query)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            clearSearchHistoryUseCase()
        }
    }

    private fun fetchSearchHistory() {
        getSearchHistoryUseCase().onEach {
            _searchHistory.value = it.map { it.query }
            println("Search History: ${_searchHistory.value}")
        }.launchIn(viewModelScope)
    }

    private fun insertSearchQuery(query: String) {
        viewModelScope.launch {
            insertSearchQueryUseCase(query)
        }
    }
}