package am.innline.book.favorites.data.worker

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.entity.BookEntity
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownloadBooksWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val bookDao: BookDao,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val filePath = inputData.getString("filePath") ?: return Result.failure()
            val syncStrategy = inputData.getString("syncStrategy")
            val file = File(filePath)

            if (!file.exists()) return Result.failure()

            val booksJson = file.readText()
            val newBooks = parseBooksJson(booksJson)
            val newBookIds = newBooks.map { it.id }.toSet()

            when (syncStrategy) {
                "optimized" -> optimizedSync(newBooks, newBookIds)
                else -> fullRefresh(newBooks)
            }.also {
                file.delete() // Clean up temp file
            }
        } catch (e: Exception) {
            Log.e("DownloadBooksWorker", "Error", e)
            Result.failure()
        }
    }

    private suspend fun optimizedSync(
        newBooks: List<Book>,
        newBookIds: Set<String>,
    ): Result {
        return withContext(Dispatchers.IO) {
            val existingBooks = mutableListOf<BookEntity>()

            bookDao.getAllBooks().onEach {
                existingBooks.clear()
                existingBooks.addAll(it)
            }

            // Step 1: Get existing books from DB
            val existingBookIds = existingBooks.map { it.id }.toSet()

            // Step 2: Identify books to add/update/delete
            val booksToAdd = newBooks.filter { it.id !in existingBookIds }
            val booksToUpdate = newBooks.filter { it.id in existingBookIds }
            val booksToDelete = existingBookIds - newBookIds

            // Step 3: Process in batches for efficiency
            var processed = 0

            // Delete obsolete books
            if (booksToDelete.isNotEmpty()) {
                bookDao.deleteByIds(booksToDelete.toList())
            }

            // Insert new books
            booksToAdd.chunked(50).forEach { chunk ->
                val entities = chunk.map { book ->
                    val imagePath = saveImageToLocal(book.thumbnailUrl, book.id)
                    BookEntity(
                        id = book.id,
                        title = book.title,
                        authors = book.authors.joinToString(", "),
                        description = book.description,
                        imagePath = imagePath
                    )
                }
                bookDao.insertBooks(entities)
                processed += chunk.size
                setProgress(workDataOf("progress" to processed))
            }

            // Update existing books
            booksToUpdate.chunked(50).forEach { chunk ->
                val entities = chunk.map { book ->
                    val imagePath = saveImageToLocal(book.thumbnailUrl, book.id)
                    BookEntity(
                        id = book.id,
                        title = book.title,
                        authors = book.authors.joinToString(", "),
                        description = book.description,
                        imagePath = imagePath
                    )
                }
                bookDao.updateBooks(entities)
                processed += chunk.size
                setProgress(workDataOf("progress" to processed))
            }

            Result.success()
        }
    }

    private suspend fun fullRefresh(books: List<Book>): Result {
        // Original implementation (clear all + insert new)
        bookDao.clearAllBooks()

        books.forEachIndexed { index, book ->
            val imagePath = saveImageToLocal(book.thumbnailUrl, book.id)
            val entity = BookEntity(
                id = book.id,
                title = book.title,
                authors = book.authors.joinToString(", "),
                description = book.description,
                imagePath = imagePath
            )
            bookDao.insertBooks(listOf(entity))
            setProgress(workDataOf("progress" to index + 1))
        }

        return Result.success()
    }

    private suspend fun saveImageToLocal(imageUrl: String?, bookId: String): String {
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

        if (bitmap != null) {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
            return file.absolutePath
        } else {
            throw IOException("Failed to download image")
        }
    }
}
