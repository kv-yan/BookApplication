package am.innline.book.favorites.domain.usecase

import am.innline.book.search.domain.model.Book
import am.innline.book.search.domain.repo.BookRepository
import kotlinx.coroutines.flow.Flow

interface FetchBooksFromApiUseCase {
    operator fun invoke(booksIds: List<String>): Flow<List<Book>>
}

class FetchBooksFromApiUseCaseImpl(private val repository: BookRepository) : FetchBooksFromApiUseCase {
    override operator fun invoke(booksIds: List<String>): Flow<List<Book>> {
        return repository.getFavoriteBooks(booksIds)
    }
}