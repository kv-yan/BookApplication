package am.innline.book.common_data.dao

import am.innline.book.common_data.entity.BookEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("DELETE FROM BookEntity")
    suspend fun clearAllBooks()

    @Query("DELETE FROM BookEntity WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Update
    suspend fun updateBooks(books: List<BookEntity>)
}
