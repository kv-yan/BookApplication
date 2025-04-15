package am.innline.book.details.domain.usecase

import am.innline.book.details.domain.repo.DownloadedBooksRepository
import am.innline.book.favorites.domain.model.BookDetails
import kotlinx.coroutines.flow.Flow

interface GetBookDetailsFromStorageUseCase {
    operator fun invoke(bookId: String): Flow<BookDetails?>
}

class GetBookDetailsFromStorageUseCaseImpl(
    private val localBookRepository: DownloadedBooksRepository,
) :
    GetBookDetailsFromStorageUseCase {
    override fun invoke(bookId: String) = localBookRepository.getBookById(bookId)
}
