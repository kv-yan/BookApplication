package am.innline.book.search.data.mediator

import am.innline.book.common_data.dao.BookDao
import am.innline.book.common_data.entity.CachedBook
import am.innline.book.search.data.api.GoogleBooksApi
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.util.UUID

@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediator(
    private val api: GoogleBooksApi,
    private val bookDao: BookDao,
    private val query: String,
) : RemoteMediator<Int, CachedBook>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedBook>,
    ): MediatorResult {
        return try {
            // Calculate page number based on load type
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    // Clear cache for this query when refreshing
                    bookDao.deleteCachedBooksForQuery(query)
                    0
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Get the last item to determine next page
                    val lastItem = state.lastItemOrNull()
                    val nextPage = (lastItem?.timestamp ?: 0) + 1
                    nextPage
                }
            }

            val response = api.searchBooks(
                query = query,
                startIndex = (page * state.config.pageSize).toInt(),
                maxResults = state.config.pageSize
            )

            val books = response.items?.map { bookDto ->
                CachedBook(
                    id = bookDto.id,
                    title = bookDto.volumeInfo.title,
                    authors = bookDto.volumeInfo.authors?.joinToString() ?: "Unknown",
                    thumbnailUrl = bookDto.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://") ?: "",
                    description = bookDto.volumeInfo.description,
                    searchQuery = query,
                    timestamp = System.currentTimeMillis()
                )
            } ?: emptyList()

            // Insert new books into database
            if (books.isNotEmpty()) {
                bookDao.insertAll(books)
            }

            MediatorResult.Success(
                endOfPaginationReached = books.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}