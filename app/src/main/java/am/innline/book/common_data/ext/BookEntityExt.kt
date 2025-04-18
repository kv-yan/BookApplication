package am.innline.book.common_data.ext

import am.innline.book.common_data.entity.BookEntity
import am.innline.book.search.domain.model.Book

fun BookEntity.Companion.fromBook(book: Book, imagePath: String?): BookEntity {
    return BookEntity(
        id = book.id,
        title = book.title,
        authors = book.authors.joinToString(", "),
        description = book.description,
        imagePath = imagePath ?: ""
    )
}