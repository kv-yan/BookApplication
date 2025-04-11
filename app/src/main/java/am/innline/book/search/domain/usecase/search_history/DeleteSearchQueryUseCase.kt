package am.innline.book.search.domain.usecase.search_history

import am.innline.book.search.domain.repo.SearchHistoryRepository

interface DeleteSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}

class DeleteSearchQueryUseCaseImpl(
    private val repository: SearchHistoryRepository,
) : DeleteSearchQueryUseCase {
    override suspend operator fun invoke(query: String) {
        repository.deleteSearchQuery(query)
    }
}