package am.innline.book.search.presentation

import am.innline.book.common_presentation.ui.theme.ScreenBackground
import am.innline.book.favorites.presentation.FavoritesViewModel
import am.innline.book.search.presentation.components.BookListContent
import am.innline.book.search.presentation.components.EmptyResultsView
import am.innline.book.search.presentation.components.ErrorView
import am.innline.book.search.presentation.components.FullScreenLoader
import am.innline.book.search.presentation.components.NoInternetView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(),
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    navigateToDetails: (String, Boolean) -> Unit,
) {
    val books = searchViewModel.pagingData.collectAsLazyPagingItems()
    val searchState by searchViewModel.searchState.collectAsState()
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val searchHistory by searchViewModel.searchHistory.collectAsState()
    val favoriteBooks by favoritesViewModel.favoriteIds.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        BooksSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            value = searchQuery,
            searchHistory = searchHistory,
            onValueChange = searchViewModel::setSearchQuery,
            onSearch = searchViewModel::triggerSearch,
            onSearchItemDelete = searchViewModel::deleteSearchItem,
            onClearSearchHistory = searchViewModel::clearSearchHistory
        )

        when (val state = searchState) {
            SearchUiState.InitialLoading -> FullScreenLoader()
            SearchUiState.NoInternet -> NoInternetView(onRetry = searchViewModel::retry)
            is SearchUiState.Error -> ErrorView(
                message = state.message,
                onRetry = searchViewModel::retry
            )

            SearchUiState.NoResults -> EmptyResultsView(searchQuery = searchQuery)
            else -> {
                BookListContent(
                    books = books,
                    favoriteBooks = favoriteBooks,
                    searchState = searchState,
                    onFavoriteClick = favoritesViewModel::toggleFavorite,
                    navigateToDetails = navigateToDetails
                )
            }
        }
    }
}