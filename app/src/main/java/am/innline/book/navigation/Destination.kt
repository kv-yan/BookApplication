package am.innline.book.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object Search : Destination()

    @Serializable
    data object Details : Destination()

    @Serializable
    data object Favorites : Destination()
}