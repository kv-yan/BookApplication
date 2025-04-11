package am.innline.book.search.data.mapper

import am.innline.book.common_data.entity.CachedBook
import am.innline.book.search.data.model.BookItemDto
import am.innline.book.search.domain.model.Book

fun BookItemDto.toDomain(): Book {
    return Book(
        id = id,
        title = volumeInfo.title,
        authors = volumeInfo.authors.orEmpty(),
        description = volumeInfo.description.orEmpty(),
        thumbnailUrl = this.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://") ?: ""
    )
}


fun CachedBook.toDomain(): Book {
    return Book(
        id = id,
        title = title,
        authors = listOf(authors),
        description = description ?: "",
        thumbnailUrl = thumbnailUrl
    )
}



