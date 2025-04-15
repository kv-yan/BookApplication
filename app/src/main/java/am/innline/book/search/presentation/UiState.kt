package am.innline.book.search.presentation

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object InitialLoading : SearchUiState
    data object PaginationLoading : SearchUiState
    data class Error(val message: String) : SearchUiState
    data object NoResults : SearchUiState
    data object NoInternet : SearchUiState
}
