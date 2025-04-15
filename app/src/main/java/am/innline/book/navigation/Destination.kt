package am.innline.book.navigation

import kotlinx.serialization.Serializable

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