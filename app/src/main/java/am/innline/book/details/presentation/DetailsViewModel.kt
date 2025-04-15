package am.innline.book.details.presentation

import am.innline.book.details.domain.usecase.GetBookDetailsFromApiUseCase
import am.innline.book.details.domain.usecase.GetBookDetailsFromStorageUseCase
import android.util.Log
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
            loadBookWithFallback(bookId)
        } else {
            loadBookFromNetwork(bookId)
        }
    }

    private fun loadBookWithFallback(bookId: String) {
        _uiState.value = DetailsUiState.Loading
        getBookDetailsFromStorageUseCase(bookId)
            .onEach { localBook ->
                if (localBook != null) {
                    _uiState.value = DetailsUiState.Success(book = localBook, loadLocally = true)
                } else {
                    // Local data not found, try network
                    loadBookFromNetwork(bookId)
                }
            }
            .catch { localError ->
                // Local load failed, try network
                Log.e("DetailsViewModel", "Failed to load from local storage")
                loadBookFromNetwork(bookId)
            }
            .launchIn(viewModelScope)
    }

    private fun loadBookFromNetwork(bookId: String) {
        getBookDetailsFromApiUseCase(bookId)
            .onEach { networkBook ->
                _uiState.value = DetailsUiState.Success(book = networkBook, loadLocally = false)
            }
            .catch { networkError ->
                _uiState.value = DetailsUiState.Error(
                    message = networkError.message ?: "Failed to load book details",
                    shouldRetry = true
                )
            }
            .launchIn(viewModelScope)
    }
}

