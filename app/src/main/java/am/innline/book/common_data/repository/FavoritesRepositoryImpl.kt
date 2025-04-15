package am.innline.book.common_data.repository

import am.innline.book.common_domain.repository.FavoritesRepository
import am.innline.book.favorites.domain.model.FavoriteBook
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")


class FavoritesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : FavoritesRepository {
    private object PreferencesKeys {
        val FAVORITES = stringSetPreferencesKey("favorite_books")
    }

    override fun getFavorites(): Flow<List<FavoriteBook>> =
        dataStore.data
            .map { preferences ->
                val favoritesSet = preferences[PreferencesKeys.FAVORITES] ?: emptySet()
                favoritesSet.map { serializedBook ->
                    val parts = serializedBook.split(DELIMITER, limit = 2)
                    FavoriteBook(id = parts[0], title = parts.getOrElse(1) { "Unknown" })
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun addToFavorites(book: FavoriteBook) {
        dataStore.edit { preferences ->
            val currentFavorites =
                preferences[PreferencesKeys.FAVORITES]?.toMutableSet() ?: mutableSetOf()
            currentFavorites.add(serializeBook(book))
            preferences[PreferencesKeys.FAVORITES] = currentFavorites
        }
    }

    override suspend fun removeFromFavorites(book: FavoriteBook) {
        dataStore.edit { preferences ->
            val currentFavorites =
                preferences[PreferencesKeys.FAVORITES]?.toMutableSet() ?: mutableSetOf()
            currentFavorites.remove(serializeBook(book))
            preferences[PreferencesKeys.FAVORITES] = currentFavorites
        }
    }

    private fun serializeBook(book: FavoriteBook): String {
        return "${book.id}$DELIMITER${book.title}"
    }

    companion object {
        private const val DELIMITER = "|||" // Choose a delimiter that won't appear in titles
    }
}
