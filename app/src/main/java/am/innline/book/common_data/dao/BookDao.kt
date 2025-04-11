package am.innline.book.common_data.dao

import am.innline.book.common_data.entity.CachedBook
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // Insert cached books
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<CachedBook>)

    // Insert a single cached book
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<CachedBook>)

    // Get cached books for specific query
    @Query("SELECT * FROM cached_books WHERE searchQuery = :query ORDER BY timestamp DESC")
    fun getCachedBooks(query: String): Flow<List<CachedBook>>

    // Get all cached books when query is empty
    @Query("SELECT * FROM cached_books ORDER BY timestamp DESC")
    fun getAllCachedBooks(): Flow<List<CachedBook>>

    // Delete old cached books based on timestamp
    @Query("DELETE FROM cached_books WHERE timestamp < :threshold")
    suspend fun deleteOldCachedBooks(threshold: Long)

    // Delete cached books for specific query
    @Query("DELETE FROM cached_books WHERE searchQuery = :query")
    suspend fun deleteCachedBooksForQuery(query: String)

    // Delete all cached books
    @Query("DELETE FROM cached_books")
    suspend fun deleteAllCachedBooks()

    // Optional: Get all unique search queries in cache
    @Query("SELECT DISTINCT searchQuery FROM cached_books")
    fun getAllCachedQueries(): Flow<List<String>>

    @Query("SELECT * FROM cached_books WHERE searchQuery = :query ORDER BY timestamp DESC")
    fun getBooksPagingSource(query: String): PagingSource<Int, CachedBook>
}