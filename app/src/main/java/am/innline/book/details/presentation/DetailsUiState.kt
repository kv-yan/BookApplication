package am.innline.book.details.presentation

import am.innline.book.favorites.domain.model.BookDetails

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data class Success(val book: BookDetails, val loadLocally: Boolean = false) : DetailsUiState()
    data class Error(val message: String, val shouldRetry: Boolean) : DetailsUiState()

}