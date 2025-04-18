package am.innline.book.favorites.data.worker

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.entity.BookEntity
import am.innline.book.common_data.ext.fromBook
import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.model.parseBooksJson
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DownloadBooksWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val bookDao: BookDao,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val filePath = inputData.getString("filePath") ?: return Result.failure()
            val totalBooks = inputData.getInt("totalBooks", 0)
            val file = File(filePath)

            if (!file.exists()) return Result.failure()

            val booksJson = file.readText()
            val newBooks = parseBooksJson(booksJson)

            // Initial progress update
            setProgress(workDataOf("progress" to 0, "totalBooks" to totalBooks))

            // Process books one by one
            newBooks.forEachIndexed { index, book ->
                processSingleBook(book, index + 1, totalBooks)
            }

            file.delete() // Clean up temp file
            Result.success()
        } catch (e: Exception) {
            Log.e("DownloadBooksWorker", "Error", e)
            Result.failure()
        }
    }

    private suspend fun processSingleBook(book: Book, currentProgress: Int, totalBooks: Int) {
        try {
            // Report progress before starting work on this book
            setProgress(
                workDataOf(
                    "progress" to currentProgress - 1, // Show previous progress
                    "totalBooks" to totalBooks
                )
            )

            // Download image first
            val imagePath = saveImageToLocal(book.thumbnailUrl, book.id)

            // Check if book exists
            val existingBook = bookDao.getBookById(book.id)

            // Create/update entity
            val entity = BookEntity.fromBook(book, imagePath)

            if (existingBook == null) {
                bookDao.insertBooks(listOf(entity))
            } else {
                bookDao.updateBooks(listOf(entity))
            }

            // Report progress after this book is processed
            setProgress(
                workDataOf(
                    "progress" to currentProgress,
                    "totalBooks" to totalBooks
                )
            )

            // Small delay to make progress visible (reduce this if it's too slow)
            delay(500)

        } catch (e: Exception) {
            Log.e("DownloadBooksWorker", "Error processing book ${book.id}", e)
            // Continue with next book even if one fails
            // Still report progress for failed book
            setProgress(
                workDataOf(
                    "progress" to currentProgress,
                    "totalBooks" to totalBooks
                )
            )
        }
    }

    private suspend fun saveImageToLocal(imageUrl: String?, bookId: String): String? {
        if (imageUrl.isNullOrBlank()) return null

        return try {
            val fileName = "$bookId.jpg"
            val file = File(applicationContext.filesDir, fileName)

            val loader = ImageLoader(applicationContext)
            val request = ImageRequest.Builder(applicationContext)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = loader.execute(request)
            val drawable = (result as? SuccessResult)?.drawable
            val bitmap = (drawable as? BitmapDrawable)?.bitmap

            bitmap?.let {
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { stream ->
                        it.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    }
                }
                file.absolutePath
            }
        } catch (e: Exception) {
            null // Return null if image download fails
        }
    }
}