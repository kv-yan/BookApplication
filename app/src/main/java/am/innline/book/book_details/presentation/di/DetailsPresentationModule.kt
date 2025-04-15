package am.innline.book.book_details.presentation.di

import am.innline.book.book_details.presentation.DetailsViewModel
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