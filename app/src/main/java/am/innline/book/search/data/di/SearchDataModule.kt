package am.innline.book.search.data.di

import am.innline.book.common_data.api.RetrofitClient
import am.innline.book.search.data.api.GoogleBooksApi
import am.innline.book.search.data.repo.BookRepositoryImpl
import am.innline.book.search.data.repo.SearchHistoryRepositoryImpl
import am.innline.book.search.domain.repo.BookRepository
import am.innline.book.search.domain.repo.SearchHistoryRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val searchDataModule = module {
    singleOf(::SearchHistoryRepositoryImpl) { bind<SearchHistoryRepository>() }
    single<BookRepository> {
        BookRepositoryImpl(
            api = get(),
            bookDao = get(),
        )
    }
    single<GoogleBooksApi> { RetrofitClient.booksApi }
}