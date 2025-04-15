package am.innline.book.favorites.domain.usecase

import am.innline.book.common_domain.repository.FavoritesRepository
import am.innline.book.favorites.domain.model.FavoriteBook

interface RemoveFavoriteBookUseCase {
    suspend operator fun invoke(book: FavoriteBook)
}


class RemoveFavoriteBookUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
) : RemoveFavoriteBookUseCase {
    override suspend fun invoke(book: FavoriteBook) =
        favoritesRepository.removeFromFavorites(book)
}