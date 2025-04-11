package am.innline.book.search.domain.usecase.search_history

import am.innline.book.search.domain.repo.SearchHistoryRepository

interface ClearSearchHistoryUseCase {
    suspend operator fun invoke()
}

class ClearSearchHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository,
) : ClearSearchHistoryUseCase {
    override suspend fun invoke() {
        repository.clearSearchHistory()
    }
}