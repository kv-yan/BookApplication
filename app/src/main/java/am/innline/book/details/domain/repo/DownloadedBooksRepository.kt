package am.innline.book.details.domain.repo

import am.innline.book.favorites.domain.model.BookDetails
import am.innline.book.search.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface DownloadedBooksRepository {
    fun getBookById(bookId: String): Flow<BookDetails?>

    fun getAllDownloadedBooks(): Flow<List<Book>>
}