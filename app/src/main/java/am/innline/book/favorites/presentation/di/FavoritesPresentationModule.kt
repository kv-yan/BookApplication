package am.innline.book.favorites.presentation.di

import am.innline.book.favorites.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val favoritesPresentationModule = module {
    viewModelOf(::FavoritesViewModel)
}