package am.innline.book.search.data.mapper

import am.innline.book.search.data.model.BookItemDto
import am.innline.book.search.domain.model.Book

fun BookItemDto.toDomain(): Book {
    return Book(
        id = id,
        title = volumeInfo.title,
        authors = volumeInfo.authors.orEmpty(),
        description = volumeInfo.description.orEmpty(),
        thumbnailUrl = volumeInfo.imageLinks?.thumbnail
    )
}

fun List<BookItemDto>?.toDomainList(): List<Book> {
    return this?.map { it.toDomain() } ?: emptyList()
}
