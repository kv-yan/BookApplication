package am.innline.book.search.presentation.components

import am.innline.book.common_presentation.item.BookItem
import am.innline.book.favorites.domain.model.FavoriteBook
import am.innline.book.search.domain.model.Book
import am.innline.book.search.presentation.ErrorItem
import am.innline.book.search.presentation.SearchUiState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun BookListContent(
    books: LazyPagingItems<Book>,
    favoriteBooks: List<FavoriteBook>,
    searchState: SearchUiState,
    onFavoriteClick: (Book) -> Unit,
    navigateToDetails: (String, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(books.itemCount) { index ->
            books[index]?.let { book ->
                val isFavorite = favoriteBooks.any { it.id == book.id }
                BookItem(
                    book = book,
                    isFavorite = isFavorite,
                    onFavoriteClick = { onFavoriteClick(book) },
                    onItemClick = { navigateToDetails(book.id, false) }
                )
            }
        }

        when (searchState) {
            SearchUiState.PaginationLoading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator()
                    }
                }
            }

            is SearchUiState.Error -> {
                item {
                    ErrorItem(
                        message = searchState.message,
                        onClickRetry = { books.retry() }
                    )
                }
            }

            else -> {}
        }
    }
}
