package am.innline.book.common_presentation.item

import am.inline.book.R
import am.innline.book.search.domain.model.Book
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
) {
    var cantShowImage by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (cantShowImage)
                Image(
                    modifier = Modifier.size(width = 80.dp, height = 120.dp),
                    painter = painterResource(id = R.drawable.book_image_not_available),
                    contentDescription = null
                )
            else
                AsyncImage(
                    modifier = Modifier.size(80.dp, 120.dp),
                    model = book.thumbnailUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    onError = {
                        cantShowImage = true
                    }
                )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 120.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 16.dp),
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        val icons =
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        val tint = if (isFavorite) Color.Red else Color.Gray
                        Icon(
                            imageVector = icons,
                            tint = tint,
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                    text = book.authors.joinToString(),
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}