package am.innline.book.search.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Book(
    val id: String,
    val title: String,
    val authors: List<String>,
    val description: String,
    val thumbnailUrl: String?,
)


fun parseBooksJson(json: String): List<Book> {
    return Json.decodeFromString(json)
}
