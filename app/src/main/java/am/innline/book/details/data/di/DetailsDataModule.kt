package am.innline.book.details.data.di

import am.innline.book.details.data.repo.DownloadedBooksRepositoryImpl
import am.innline.book.details.domain.repo.DownloadedBooksRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val detailsDataModule = module {
    singleOf(::DownloadedBooksRepositoryImpl) { bind<DownloadedBooksRepository>() }
}