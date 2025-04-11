package am.innline.book.search.domain.di

import am.innline.book.search.domain.usecase.book.GetSearchBooksUseCase
import am.innline.book.search.domain.usecase.book.GetSearchBooksUseCaseImpl
import am.innline.book.search.domain.usecase.search_history.ClearSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.ClearSearchHistoryUseCaseImpl
import am.innline.book.search.domain.usecase.search_history.DeleteSearchQueryUseCase
import am.innline.book.search.domain.usecase.search_history.DeleteSearchQueryUseCaseImpl
import am.innline.book.search.domain.usecase.search_history.GetSearchHistoryUseCase
import am.innline.book.search.domain.usecase.search_history.GetSearchHistoryUseCaseImpl
import am.innline.book.search.domain.usecase.search_history.InsertSearchQueryUseCase
import am.innline.book.search.domain.usecase.search_history.InsertSearchQueryUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchDomainModule = module {
    // book
    factoryOf(::GetSearchBooksUseCaseImpl) { bind<GetSearchBooksUseCase>() }

    // search history
    factoryOf(::ClearSearchHistoryUseCaseImpl) { bind<ClearSearchHistoryUseCase>() }
    factoryOf(::DeleteSearchQueryUseCaseImpl) { bind<DeleteSearchQueryUseCase>() }
    factoryOf(::GetSearchHistoryUseCaseImpl) { bind<GetSearchHistoryUseCase>() }
    factoryOf(::InsertSearchQueryUseCaseImpl) { bind<InsertSearchQueryUseCase>() }

}