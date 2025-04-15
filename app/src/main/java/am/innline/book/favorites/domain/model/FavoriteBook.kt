package am.innline.book.favorites.domain.model

data class FavoriteBook(
    val id: String,
    val title: String,
    val author: String? = null,
    val description: String ? = null,
    val imageUri: String?= null,
)