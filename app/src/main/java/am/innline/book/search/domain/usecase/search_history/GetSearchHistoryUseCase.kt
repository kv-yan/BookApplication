package am.innline.book.search.domain.usecase.search_history

import am.innline.book.common_data.entity.SearchQuery
import am.innline.book.search.domain.repo.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

interface GetSearchHistoryUseCase {
    operator fun invoke(): Flow<List<SearchQuery>>
}

class GetSearchHistoryUseCaseImpl(
    private val repository: SearchHistoryRepository,
) : GetSearchHistoryUseCase {
    override operator fun invoke(): Flow<List<SearchQuery>> {
        return repository.getRecentSearchQueries()
    }

}