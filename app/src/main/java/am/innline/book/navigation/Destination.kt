package am.innline.book.navigation

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed class Destination {
    @Serializable
    data object Search : Destination()

    @Serializable
    data class Details(
        val bookId: String,
        val loadLocally: Boolean,
    ) : Destination()

    @Serializable
    data object Favorites : Destination()
}