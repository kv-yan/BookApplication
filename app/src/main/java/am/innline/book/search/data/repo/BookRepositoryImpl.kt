package am.innline.book.search.data.repo

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.entity.CachedBook
import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.mapper.toDomain
import am.innline.book.search.data.mediator.BookRemoteMediator
import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class BookRepositoryImpl(
    private val api: GoogleBooksApi,
    private val bookDao: BookDao,
//    private val searchDao: SearchDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BookRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getSearchResultStream(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = BookRemoteMediator(api, bookDao, query),
            pagingSourceFactory = { bookDao.getBooksPagingSource(query) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    suspend fun clearCacheForQuery(query: String) {
        withContext(dispatcher) {
            bookDao.deleteCachedBooksForQuery(query)
        }
    }

    suspend fun clearAllCache() {
        withContext(dispatcher) {
            bookDao.deleteAllCachedBooks()
        }
    }

    fun getAllCachedBooks(query: String): Flow<List<CachedBook>> {
        return bookDao.getCachedBooks(query) // Empty query returns all
    }
}