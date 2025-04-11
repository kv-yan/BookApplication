package am.innline.book.common_data.api

import am.innline.book.search.data.api.GoogleBooksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val booksApi: GoogleBooksApi by lazy {
        retrofit.create(GoogleBooksApi::class.java)
    }
}
