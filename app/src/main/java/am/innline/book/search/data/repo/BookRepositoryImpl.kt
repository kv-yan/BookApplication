package am.innline.book.search.data.repo

import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.mediator.BookPagingSource
import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
}