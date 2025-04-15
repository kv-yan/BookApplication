package am.innline.book.book_details.presentation

import am.innline.book.search.domain.model.Book

sealed class DetailsUiState {
    data object Loading : DetailsUiState()
    data class Success(val book: Book) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()

}