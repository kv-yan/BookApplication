package am.innline.book.details.domain.di

import am.innline.book.details.domain.usecase.GetBookDetailsFromApiUseCase
import am.innline.book.details.domain.usecase.GetBookDetailsFromApiUseCaseImpl
import am.innline.book.details.domain.usecase.GetBookDetailsFromStorageUseCase
import am.innline.book.details.domain.usecase.GetBookDetailsFromStorageUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val detailsDomainModule = module {
    factoryOf(::GetBookDetailsFromApiUseCaseImpl) { bind<GetBookDetailsFromApiUseCase>() }
    factoryOf(::GetBookDetailsFromStorageUseCaseImpl) { bind<GetBookDetailsFromStorageUseCase>() }

}