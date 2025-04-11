package am.innline.book.common_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQuery(
    @PrimaryKey val query: String,
    val timestamp: Long = System.currentTimeMillis(),
)