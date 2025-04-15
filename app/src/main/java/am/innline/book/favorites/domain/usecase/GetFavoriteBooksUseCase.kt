package am.innline.book.favorites.domain.usecase

import am.innline.book.common_domain.repository.FavoritesRepository
import am.innline.book.favorites.domain.model.FavoriteBook
import kotlinx.coroutines.flow.Flow

interface GetFavoriteBooksUseCase {
    operator fun invoke(): Flow<List<FavoriteBook>>
}


class GetFavoriteBooksUseCaseImpl(
    private val favoritesRepository: FavoritesRepository,
) : GetFavoriteBooksUseCase {
    override operator fun invoke(): Flow<List<FavoriteBook>> {
        return favoritesRepository.getFavorites()
    }
}
