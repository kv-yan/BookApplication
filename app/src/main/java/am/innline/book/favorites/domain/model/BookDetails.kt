package am.innline.book.favorites.domain.model

data class BookDetails(
    val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val imageUri: String,
)