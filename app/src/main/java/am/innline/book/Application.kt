package am.innline.book

import am.innline.book.common_data.di.commonDataModule
import am.innline.book.search.data.di.searchDataModule
import am.innline.book.search.domain.di.searchDomainModule
import am.innline.book.search.presentation.di.searchPresentationModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                commonDataModule,
                searchPresentationModule,
                searchDomainModule,
                searchDataModule
            )
        }
    }
}