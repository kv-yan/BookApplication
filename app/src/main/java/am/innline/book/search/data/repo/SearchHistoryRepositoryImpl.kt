package am.innline.book.search.data.repo

import am.innline.book.common_data.dao.SearchDao
import am.innline.book.common_data.entity.SearchQuery
import am.innline.book.search.domain.repo.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchHistoryRepositoryImpl(
    private val searchDao: SearchDao,
) : SearchHistoryRepository {
    override fun getRecentSearchQueries(): Flow<List<SearchQuery>> =
        searchDao.getRecentSearchQueries().flowOn(Dispatchers.IO)

    override suspend fun insertNewSearchQuery(query: String) =
        searchDao.insertSearchQuery(SearchQuery(query))

    override suspend fun deleteSearchQuery(query: String) = searchDao.deleteSearchQueryByText(query)

    override suspend fun clearSearchHistory() = searchDao.deleteAllSearchQueries()
}