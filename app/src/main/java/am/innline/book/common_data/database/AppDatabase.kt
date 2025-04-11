package am.innline.book.common_data.database

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.dao.SearchDao
import am.innline.book.common_data.entity.CachedBook
import am.innline.book.common_data.entity.SearchQuery
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CachedBook::class, SearchQuery::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun searchDao(): SearchDao


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