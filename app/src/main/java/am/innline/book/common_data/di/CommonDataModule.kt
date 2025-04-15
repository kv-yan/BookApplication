package am.innline.book.common_data.di

import am.innline.book.common_data.database.AppDatabase
import am.innline.book.common_data.repository.FavoritesRepositoryImpl
import am.innline.book.common_data.repository.favoritesDataStore
import am.innline.book.common_domain.repository.FavoritesRepository
import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonDataModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().searchDao() }
    single { get<AppDatabase>().bookDao() }
    single<CoroutineDispatcher> { Dispatchers.IO }
    singleOf(::FavoritesRepositoryImpl) { bind<FavoritesRepository>() }
    single<DataStore<Preferences>> { get<Context>().favoritesDataStore }
    single<ConnectivityManager> { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
}

