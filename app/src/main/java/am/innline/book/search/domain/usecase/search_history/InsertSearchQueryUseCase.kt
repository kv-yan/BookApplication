package am.innline.book.search.domain.usecase.search_history

import am.innline.book.search.domain.repo.SearchHistoryRepository

interface InsertSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}

class InsertSearchQueryUseCaseImpl(
    private val repository: SearchHistoryRepository,
) : InsertSearchQueryUseCase {
    override suspend operator fun invoke(query: String) {
        repository.insertNewSearchQuery(query)
    }

}