package am.innline.book.search.data.model


data class BookResponseDto(
    val totalItems: Int,
    val items: List<BookItemDto>?
)

