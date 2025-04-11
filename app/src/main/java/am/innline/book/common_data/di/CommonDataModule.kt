package am.innline.book.common_data.di

import am.innline.book.common_data.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val commonDataModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().bookDao() }
    single { get<AppDatabase>().searchDao() }
    single<CoroutineDispatcher> { Dispatchers.IO }
}