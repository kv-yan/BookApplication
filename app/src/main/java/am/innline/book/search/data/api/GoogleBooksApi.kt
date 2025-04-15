package am.innline.book.search.data.api

import am.innline.book.search.data.model.BookItemDto
import am.innline.book.search.data.model.BookResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApi {
    // For search screen
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int = 10,
        @Query("fields") fields: String = SEARCH_RESPONSE_FIELDS
    ): BookResponseDto

    // For getting single book details
    @GET("volumes/{bookId}")
    suspend fun getBookById(
        @Path("bookId") bookId: String,
        @Query("fields") fields: String = SINGLE_BOOK_FIELDS
    ): BookItemDto

    companion object {
        // Fields for search results (lightweight)
        const val SEARCH_RESPONSE_FIELDS = "totalItems,items(id,volumeInfo(title,authors,imageLinks/thumbnail))"

        // Fields for single book details (complete info)
        const val SINGLE_BOOK_FIELDS = "id,volumeInfo(title,authors,description,imageLinks/thumbnail)"
    }
}