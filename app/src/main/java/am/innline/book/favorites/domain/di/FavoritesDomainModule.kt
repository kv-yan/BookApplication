package am.innline.book.favorites.domain.di

import am.innline.book.favorites.domain.usecase.AddFavoriteBookUseCase
import am.innline.book.favorites.domain.usecase.AddFavoriteBookUseCaseImpl
import am.innline.book.favorites.domain.usecase.GetFavoriteBooksUseCase
import am.innline.book.favorites.domain.usecase.GetFavoriteBooksUseCaseImpl
import am.innline.book.favorites.domain.usecase.RemoveFavoriteBookUseCase
import am.innline.book.favorites.domain.usecase.RemoveFavoriteBookUseCaseImpl
import am.innline.book.favorites.domain.usecase.FetchBooksFromApiUseCase
import am.innline.book.favorites.domain.usecase.FetchBooksFromApiUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val favoritesDomainModule = module {
    factoryOf(::AddFavoriteBookUseCaseImpl) { bind<AddFavoriteBookUseCase>() }
    factoryOf(::GetFavoriteBooksUseCaseImpl) { bind<GetFavoriteBooksUseCase>() }
    factoryOf(::RemoveFavoriteBookUseCaseImpl) { bind<RemoveFavoriteBookUseCase>() }
    factoryOf(::FetchBooksFromApiUseCaseImpl) { bind<FetchBooksFromApiUseCase>() }
}

