package am.innline.book.search.data.repo

import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.mapper.toDomain
import am.innline.book.search.data.mediator.BookPagingSource
import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class BookRepositoryImpl(private val api: GoogleBooksApi) : BookRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { BookPagingSource(api, query) }
        ).flow
            .flowOn(Dispatchers.IO)
            .catch {
                throw it
            }
    }

    override fun getFavoriteBooks(list: List<String>): Flow<List<Book>> = flow {

        if (list.isEmpty()) {
            emit(emptyList())
            return@flow
        }

        val books = mutableListOf<Book>()

        list.forEach { bookId ->
            try {
                val bookItem = api.getBookById(bookId)
                books.add(bookItem.toDomain())
            } catch (e: Exception) {
                // Log error but continue with other books
                Log.e("BookRepository", "Failed to fetch book $bookId", e)
            }
        }

        emit(books)
    }.flowOn(Dispatchers.IO)

    override fun getBookById(bookId: String): Flow<Book>  = flow{
        val bookItem = api.getBookById(bookId)
        emit(bookItem.toDomain())
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

}