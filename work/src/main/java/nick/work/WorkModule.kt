package nick.work

import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module(includes = [WorkAssistedModule::class, WorkerModule::class])
object WorkModule {

    @Reusable
    @Provides
    @JvmStatic
    fun workManager() = WorkManager.getInstance()

    @Provides
    @JvmStatic
    fun configuration(workWorkerFactory: WorkWorkerFactory) = Configuration.Builder()
        .setWorkerFactory(workWorkerFactory)
        .build()
}