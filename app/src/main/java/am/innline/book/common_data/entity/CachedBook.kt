package am.innline.book.common_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_books")
data class CachedBook(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val thumbnailUrl: String,
    val description: String?,
    val searchQuery: String,
    val timestamp: Long = System.currentTimeMillis()
)