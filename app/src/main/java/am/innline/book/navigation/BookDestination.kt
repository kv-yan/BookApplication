package am.innline.book.navigation

import kotlinx.serialization.Serializable

sealed class BookDestination {
    @Serializable
    data object Search : BookDestination()

    @Serializable
    data object Favorites : BookDestination()
}

