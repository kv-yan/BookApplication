package am.innline.book.favorites.presentation

import am.innline.book.common_presentation.item.BookItem
import am.innline.book.common_presentation.ui.theme.MainBlue
import am.innline.book.common_presentation.ui.theme.ScreenBackground
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    navigateToDetails: (String, Boolean) -> Unit,
) {
    val booksState by favoritesViewModel.booksState.collectAsState()
    val favIds by favoritesViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current
    val downloadState by favoritesViewModel.downloadState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    when (val state = booksState) {
        FavoriteUiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "There is nothing here"
                )
            }
        }

        is FavoriteUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = state.message
                )
            }
        }

        FavoriteUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoriteUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(ScreenBackground)
                    .padding(top = 24.dp, bottom = 8.dp)
            ) {

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    stickyHeader {
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, top = 8.dp, end = 4.dp),
                            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MainBlue,
                                containerColor = Color.White
                            ),
                            onClick = {
                                favoritesViewModel.downloadAllFavoritesForOffline(
                                    context,
                                    lifecycleOwner
                                )
                            }
                        ) {
                            Text(text = "Download All")
                        }
                    }

                    itemsIndexed(
                        items = state.books,
                        key = { _, book -> book.id }
                    ) { _, book ->
                        BookItem(
                            book = book,
                            isFavorite = favIds.any { it.id == book.id },
                            onFavoriteClick = { favoritesViewModel.toggleFavorite(book) },
                            onItemClick = { navigateToDetails(book.id, true) }
                        )
                    }
                }
            }
        }
    }

    when (val state = downloadState) {
        is DownloadState.InProgress -> {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Downloading Books") },
                text = {
                    Column {
                        Text("Downloading ${state.current} of ${state.total} books")
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { state.current.toFloat() / state.total.toFloat() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {},
                dismissButton = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }

        DownloadState.Completed -> {
            AlertDialog(
                onDismissRequest = { /* Auto-dismissed */ },
                title = { Text("Download Complete") },
                text = { Text("All books downloaded successfully") },
                confirmButton = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }

        is DownloadState.Error -> {
            AlertDialog(
                onDismissRequest = { /* Auto-dismissed */ },
                title = { Text("Download Error") },
                text = { Text(state.message) },
                confirmButton = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }

        DownloadState.Idle -> {}
    }
}