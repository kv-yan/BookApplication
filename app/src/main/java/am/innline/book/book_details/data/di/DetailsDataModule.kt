package am.innline.book.book_details.data.di

import am.innline.book.book_details.data.repo.LocalBooksRepositoryImpl
import am.innline.book.book_details.domain.repo.LocalBooksRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsDataModule = module {
    singleOf(::LocalBooksRepositoryImpl) { bind<LocalBooksRepository>() }
}