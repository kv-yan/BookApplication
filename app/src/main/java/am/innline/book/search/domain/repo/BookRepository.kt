package am.innline.book.search.domain.repo

import am.innline.book.search.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun searchBooks(query: String, startIndex: Int, maxResults: Int): Flow<List<Book>>
}