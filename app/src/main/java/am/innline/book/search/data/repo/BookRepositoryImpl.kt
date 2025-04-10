package am.innline.book.search.data.repo

import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.mapper.toDomain
import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookRepositoryImpl(
    private val api: GoogleBooksApi,
) : BookRepository {

    override fun searchBooks(
        query: String,
        startIndex: Int,
        maxResults: Int,
    ): Flow<List<Book>> = flow {
        try {
            val response = api.searchBooks(query, startIndex, maxResults)
            val books = response.items?.map { it.toDomain() } ?: emptyList()
            emit(books)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}