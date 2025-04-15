package am.innline.book.book_details.data.ext

import am.innline.book.common_data.entity.BookEntity
import am.innline.book.favorites.domain.model.BookDetails

fun BookEntity.toBookDetails(): BookDetails {
    return BookDetails(
        id = id,
        title = title,
        authors = authors,
        description = description,
        imageUri = imagePath,
    )
}