package am.innline.book.book_details.presentation

import am.innline.book.search.domain.model.Book
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel(
    bookId: String,
    loadLocally: Boolean,
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    init {
        if (loadLocally) {
            loadBookFromDatabase()
        } else {
            loadBookFromNetwork()
        }

        println("BookId: $bookId")
        println("Load Locally: $loadLocally")
    }

    private fun loadBookFromDatabase() {}

    private fun loadBookFromNetwork() {}

}