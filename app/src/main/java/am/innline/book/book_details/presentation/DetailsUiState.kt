package am.innline.book.book_details.presentation

import am.innline.book.favorites.domain.model.BookDetails

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data class Success(val book: BookDetails, val loadLocally: Boolean = false) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()

}