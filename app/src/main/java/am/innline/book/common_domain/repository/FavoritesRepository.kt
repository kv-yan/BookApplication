package am.innline.book.common_domain.repository

import am.innline.book.favorites.domain.model.FavoriteBook
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<List<FavoriteBook>>
    suspend fun addToFavorites(book: FavoriteBook)
    suspend fun removeFromFavorites(book: FavoriteBook)
}