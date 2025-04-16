package am.innline.book.navigation

import kotlinx.serialization.Serializable

sealed class AppDestination {
    @Serializable
    data object Book : AppDestination()

    @Serializable
    data class Details(
        val bookId: String,
        val loadLocally: Boolean,
    ) : AppDestination()
}