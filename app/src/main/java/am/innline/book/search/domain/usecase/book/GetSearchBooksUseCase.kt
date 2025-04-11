package am.innline.book.search.domain.usecase.book

import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GetSearchBooksUseCase {
    operator fun invoke(query: String): Flow<PagingData<Book>>
}

class GetSearchBooksUseCaseImpl(
    private val repository: BookRepository,
) : GetSearchBooksUseCase {

    override operator fun invoke(query: String) = repository.getSearchResultStream(query)
}
