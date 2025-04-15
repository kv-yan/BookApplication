package am.innline.book.details.presentation.di

import am.innline.book.details.presentation.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsPresentationModule = module {
    viewModel { (bookId: String, loadLocally: Boolean) ->
        DetailsViewModel(
            bookId = bookId,
            loadLocally = loadLocally,
            getBookDetailsFromStorageUseCase = get(),
            getBookDetailsFromApiUseCase = get()
        )
    }
}