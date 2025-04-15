package am.innline.book.favorites.presentation

import am.innline.book.search.domain.model.Book

sealed interface FavoriteUiState {
        data object Loading : FavoriteUiState
        data class Success(val books: List<Book>) : FavoriteUiState
        data class Error(val message: String) : FavoriteUiState
        data object Empty : FavoriteUiState
    }
