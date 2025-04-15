package am.innline.book.search.presentation.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyResultsView(
    modifier: Modifier = Modifier,
    searchQuery: String,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "No results for '$searchQuery'"
        )
    }
}