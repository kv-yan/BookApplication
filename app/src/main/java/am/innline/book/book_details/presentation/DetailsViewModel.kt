package am.innline.book.book_details.presentation

import am.innline.book.book_details.domain.useacase.GetBookDetailsFromApiUseCase
import am.innline.book.book_details.domain.useacase.GetBookDetailsFromStorageUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailsViewModel(
    bookId: String,
    loadLocally: Boolean,
    private val getBookDetailsFromStorageUseCase: GetBookDetailsFromStorageUseCase,
    private val getBookDetailsFromApiUseCase: GetBookDetailsFromApiUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        if (loadLocally) {
            loadBookFromDatabase(bookId)
        } else {
            loadBookFromNetwork(bookId)
        }
    }

    private fun loadBookFromDatabase(bookId: String) {
        _uiState.value = DetailsUiState.Loading
        getBookDetailsFromStorageUseCase(bookId).onEach {
            _uiState.value = DetailsUiState.Success(book = it, loadLocally = true)
        }.catch {
            _uiState.value = DetailsUiState.Error(it.message ?: "Unknown error")
        }.launchIn(viewModelScope)
    }

    private fun loadBookFromNetwork(bookId: String) {
        _uiState.value = DetailsUiState.Loading
        getBookDetailsFromApiUseCase(bookId).onEach {
            _uiState.value = DetailsUiState.Success(book = it, loadLocally = false)
        }.catch {
            _uiState.value = DetailsUiState.Error(it.message ?: "Unknown error")
        }.launchIn(viewModelScope)

    }
}