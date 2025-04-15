package am.innline.book.details.data.repo

import am.innline.book.details.data.ext.toBook
import am.innline.book.details.data.ext.toBookDetails
import am.innline.book.details.domain.repo.DownloadedBooksRepository
import am.innline.book.common_data.dao.BookDao
import am.innline.book.search.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DownloadedBooksRepositoryImpl(
    private val bookDao: BookDao,
) : DownloadedBooksRepository {
    override fun getBookById(bookId: String) = flow {
        val bookEntity = bookDao.getBookById(bookId)
        if (bookEntity != null) {
            emit(bookEntity.toBookDetails())
        }else
            emit(null)
    }

    override fun getAllDownloadedBooks(): Flow<List<Book>> =
        bookDao.getAllBooks().map { it.map { it.toBook() } }
}

