package am.innline.book.favorites.data.di

import am.innline.book.favorites.data.worker.DownloadBooksWorker
import android.content.Context
import androidx.work.WorkerParameters
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    factory { (appContext: Context, workerParams: WorkerParameters) ->
        DownloadBooksWorker(
            appContext,
            workerParams,
            bookDao = get(),
        )
    }

    worker {
        DownloadBooksWorker(
            context = get(),
            workerParams = get(),
            bookDao = get(),
        )
    }
}
