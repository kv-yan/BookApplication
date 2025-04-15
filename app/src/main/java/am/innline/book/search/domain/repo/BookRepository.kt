package am.innline.book.search.domain.repo

import am.innline.book.search.domain.model.Book
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getSearchResultStream(query: String): Flow<PagingData<Book>>

    fun getFavoriteBooks(list: List<String>): Flow<List<Book>>

    fun getBookById(bookId: String): Flow<Book>
}