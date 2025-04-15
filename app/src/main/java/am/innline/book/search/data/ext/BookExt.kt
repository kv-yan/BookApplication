package am.innline.book.search.data.ext

import am.innline.book.favorites.domain.model.BookDetails
import am.innline.book.search.domain.model.Book

fun Book.toBookDetails(): BookDetails {
    return BookDetails(
        id = id,
        title = title,
        authors = authors.joinToString(", "),
        description = description,
        imageUri = thumbnailUrl ?: "",
    )
}