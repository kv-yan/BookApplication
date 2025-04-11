package am.innline.book.search.presentation

import am.innline.book.common_presentation.item.BookItem
import am.innline.book.common_presentation.ui.theme.ScreenBackground
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val books = viewModel.books.collectAsLazyPagingItems()
    val showInitialLoader by viewModel.showInitialLoader.collectAsState()
    val showPaginationLoader by viewModel.showPaginationLoader.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .systemBarsPadding()
    ) {
        BooksSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp
                ),
            value = viewModel.searchQuery.collectAsState().value,
            searchHistory = searchHistory,
            onValueChange = { viewModel.setSearchQuery(it) },
            onSearch = { viewModel.triggerSearch() },
            onSearchItemDelete = { viewModel.deleteSearchItem(it) },
            onClearSearchHistory = { viewModel.clearSearchHistory() }
        )

        if (showInitialLoader) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(books.itemCount) { index ->
                    books[index]?.let { book ->
                        BookItem(
                            modifier = Modifier.fillMaxWidth(),
                            book = book
                        )
                    }
                }

                // Show loading indicator at the bottom when loading more
                if (showPaginationLoader) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator()
                        }
                    }
                }

                // Add "Load More" button if there are more items to load
                if (books.loadState.append is LoadState.NotLoading && books.itemCount > 0) {
                    item {
                        Button(
                            onClick = { books.retry() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = books.loadState.append !is LoadState.Loading
                        ) {
                            Text("Load More")
                        }
                    }
                }

                // Handled error state
                if (books.loadState.append is LoadState.Error) {
                    item {
                        ErrorItem(
                            message = "Failed to load more items",
                            modifier = Modifier.fillMaxWidth(),
                            onClickRetry = { books.retry() }
                        )
                    }
                }
            }
        }
    }
}