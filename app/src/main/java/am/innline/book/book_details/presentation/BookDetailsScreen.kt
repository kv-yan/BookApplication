package am.innline.book.book_details.presentation


import am.inline.book.R
import am.innline.book.book_details.presentation.utils.isUri
import am.innline.book.common_presentation.ui.theme.ScreenBackground
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    modifier: Modifier = Modifier,
    bookId: String,
    loadLocally: Boolean,
    viewModel: DetailsViewModel = getViewModel { parametersOf(bookId, loadLocally) },
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground,
                    titleContentColor = Color.Black,
                ),
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        when (val state = uiState) {
            is DetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = Color.Red)
                }
            }

            DetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailsUiState.Success -> {
                val book = state.book
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // need to check if is uri or image link
                        AsyncImage(
                            model = if (isUri(book.imageUri)) {
                                // Local URI or file path
                                ImageRequest.Builder(LocalContext.current)
                                    .data(book.imageUri)
                                    .build()
                            } else {
                                // Assume it's a URL (or use placeholder if invalid)
                                book.imageUri
                            },
                            contentDescription = book.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.book_image_not_available),
                            placeholder = painterResource(id = R.drawable.book_image_not_available)
                        )
                    }

                    // Book Details
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        // Title
                        Text(
                            text = book.title,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Authors
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Authors",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = book.authors,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }

                        // Description
                        Text(
                            text = book.description,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = book.description,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }

            }
        }
    }
}