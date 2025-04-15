package am.innline.book.favorites.presentation

sealed class DownloadState {
        data object Idle : DownloadState()
        data class InProgress(val current: Int, val total: Int) : DownloadState()
        data object Completed : DownloadState()
        data class Error(val message: String) : DownloadState()
    }