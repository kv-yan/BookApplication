package am.innline.book.search.data.mediator

import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.mapper.toDomain
import am.innline.book.search.domain.model.Book
import androidx.paging.PagingSource
import androidx.paging.PagingState

class BookPagingSource(
    private val api: GoogleBooksApi,
    private val query: String
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            val page = params.key ?: 0 // Start from page 0
            val response = api.searchBooks(
                query = query,
                startIndex = page * params.loadSize,
                maxResults = params.loadSize
            )

            LoadResult.Page(
                data = response.items?.map { it.toDomain() } ?: emptyList(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.items.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? = null
}