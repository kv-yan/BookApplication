package am.innline.book.details.data.ext

import am.innline.book.common_data.entity.BookEntity
import am.innline.book.favorites.domain.model.BookDetails
import am.innline.book.search.domain.model.Book

fun BookEntity.toBookDetails(): BookDetails {
    return BookDetails(
        id = id,
        title = title,
        authors = authors,
        description = description,
        imageUri = imagePath,
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authors = listOf(authors),
        description = description,
        thumbnailUrl = imagePath,
    )
}