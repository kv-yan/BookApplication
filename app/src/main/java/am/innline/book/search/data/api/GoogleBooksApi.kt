package am.innline.book.search.data.api

import am.innline.book.search.data.model.BookResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int = 10,
        @Query("printType") printType: String = "books"
    ): BookResponseDto
}
