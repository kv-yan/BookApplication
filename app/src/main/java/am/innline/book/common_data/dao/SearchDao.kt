package am.innline.book.common_data.dao

import am.innline.book.common_data.entity.SearchQuery
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(query: SearchQuery)

    @Query("SELECT * FROM search_queries ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearchQueries(): Flow<List<SearchQuery>>

    @Delete
    suspend fun deleteSearchQuery(query: SearchQuery)

    @Query("DELETE FROM search_queries")
    suspend fun deleteAllSearchQueries()

    @Query("DELETE FROM search_queries WHERE query = :query")
    suspend fun deleteSearchQueryByText(query: String)
}