package am.innline.book.favorites.data.repo

import am.innline.book.search.data.model.BookItemDto
import am.innline.book.search.domain.model.Book
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface OfflineBooksRepository {
    suspend fun saveBook(book: Book, imageUri: Uri)
    suspend fun saveBook(book: BookItemDto, imageUri: Uri)
    fun getOfflineBooks(): Flow<List<Book>>
    fun getOfflineBook(id: String): Book?
    suspend fun deleteOfflineBook(id: String)
}