package am.innline.book.book_details.domain.useacase

import am.innline.book.favorites.domain.model.BookDetails
import am.innline.book.search.data.ext.toBookDetails
import am.innline.book.search.domain.repo.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetBookDetailsFromApiUseCase {
    operator fun invoke(bookId: String): Flow<BookDetails>
}

class GetBookDetailsFromApiUseCaseImpl(
    private val bookRepository: BookRepository,
) :
    GetBookDetailsFromApiUseCase {
    override fun invoke(bookId: String) =
        bookRepository.getBookById(bookId).map { it.toBookDetails() }
}
