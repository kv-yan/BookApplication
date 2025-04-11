package am.innline.book.search.domain.repo

import am.innline.book.common_data.entity.SearchQuery
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getRecentSearchQueries(): Flow<List<SearchQuery>>
    suspend fun insertNewSearchQuery(query: String)
    suspend fun deleteSearchQuery(query: String)
    suspend fun clearSearchHistory()
}