package am.innline.book.common_data.database

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.dao.SearchDao
import am.innline.book.common_data.entity.BookEntity
import am.innline.book.common_data.entity.SearchQuery
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SearchQuery::class, BookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun bookDao(): BookDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context = context.applicationContext,
                        klass = AppDatabase::class.java,
                        name = "book_database"
                    )
                    .addCallback(object : Callback() {})
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}