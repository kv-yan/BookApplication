package am.innline.book.favorites.domain.usecase

import am.innline.book.common_domain.repository.FavoritesRepository
import am.innline.book.favorites.domain.model.FavoriteBook

interface AddFavoriteBookUseCase {
    suspend operator fun invoke(book: FavoriteBook)
}


class AddFavoriteBookUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
) : AddFavoriteBookUseCase {
    override suspend fun invoke(book: FavoriteBook) =
        favoritesRepository.addToFavorites(book)
}