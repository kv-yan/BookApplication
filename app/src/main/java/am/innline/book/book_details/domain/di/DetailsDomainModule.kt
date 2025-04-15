package am.innline.book.book_details.domain.di

import am.innline.book.book_details.domain.useacase.GetBookDetailsFromApiUseCase
import am.innline.book.book_details.domain.useacase.GetBookDetailsFromApiUseCaseImpl
import am.innline.book.book_details.domain.useacase.GetBookDetailsFromStorageUseCase
import am.innline.book.book_details.domain.useacase.GetBookDetailsFromStorageUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val detailsDomainModule = module {
    factoryOf(::GetBookDetailsFromApiUseCaseImpl) { bind<GetBookDetailsFromApiUseCase>() }
    factoryOf(::GetBookDetailsFromStorageUseCaseImpl) { bind<GetBookDetailsFromStorageUseCase>() }

}