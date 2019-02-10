package nick.work.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import nick.work.worker.ChildWorkerFactory
import javax.inject.Inject
import javax.inject.Provider

class WorkWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: error("Unknown worker class name: $workerClassName")

        return factoryProvider.get().create(appContext, workerParameters)
    }
}