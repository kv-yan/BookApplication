package am.innline.book.book_details.domain.repo

import am.innline.book.favorites.domain.model.BookDetails
import kotlinx.coroutines.flow.Flow

interface LocalBooksRepository {
    fun getBookById(bookId: String): Flow<BookDetails>
}