package am.innline.book.common_data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BookEntity")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val imagePath: String // local path
)
