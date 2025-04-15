package am.innline.book

import am.innline.book.details.data.di.detailsDataModule
import am.innline.book.details.domain.di.detailsDomainModule
import am.innline.book.details.presentation.di.detailsPresentationModule
import am.innline.book.common_data.di.commonDataModule
import am.innline.book.favorites.data.di.workerModule
import am.innline.book.favorites.domain.di.favoritesDomainModule
import am.innline.book.favorites.presentation.di.favoritesPresentationModule
import am.innline.book.search.data.di.searchDataModule
import am.innline.book.search.domain.di.searchDomainModule
import am.innline.book.search.presentation.di.searchPresentationModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin

class BookApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookApplication)
            workManagerFactory()
            modules(
                commonDataModule,
                searchPresentationModule,
                searchDomainModule,
                searchDataModule,
                favoritesPresentationModule,
                favoritesDomainModule,
                workerModule,
                detailsPresentationModule,
                detailsDomainModule,
                detailsDataModule
            )
        }
    }
}