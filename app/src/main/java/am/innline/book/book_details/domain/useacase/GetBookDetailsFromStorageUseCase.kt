package am.innline.book.book_details.domain.useacase

import am.innline.book.book_details.domain.repo.LocalBooksRepository
import am.innline.book.favorites.domain.model.BookDetails
import kotlinx.coroutines.flow.Flow

interface GetBookDetailsFromStorageUseCase {
    operator fun invoke(bookId: String): Flow<BookDetails>
}

class GetBookDetailsFromStorageUseCaseImpl(
    private val localBookRepository: LocalBooksRepository,
) :
    GetBookDetailsFromStorageUseCase {
    override fun invoke(bookId: String) = localBookRepository.getBookById(bookId)
}
