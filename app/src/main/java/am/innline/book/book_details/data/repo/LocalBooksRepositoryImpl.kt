package am.innline.book.book_details.data.repo

import am.innline.book.book_details.data.ext.toBookDetails
import am.innline.book.book_details.domain.repo.LocalBooksRepository
import am.innline.book.common_data.dao.BookDao
import kotlinx.coroutines.flow.flow

class LocalBooksRepositoryImpl(
    private val bookDao: BookDao,
) : LocalBooksRepository {
    override fun getBookById(bookId: String) = flow {
        val bookEntity = bookDao.getBookById(bookId)
        if (bookEntity != null) {
            emit(bookEntity.toBookDetails())
        }

    }
}

