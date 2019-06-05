package nick.work.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.Reusable
import nick.core.di.ApplicationContext

@Module(includes = [WorkAssistedModule::class, WorkerModule::class])
object WorkModule {

    @Reusable
    @Provides
    @JvmStatic
    fun workManager(@ApplicationContext applicationContext: Context) =
        WorkManager.getInstance(applicationContext)

    @Provides
    @JvmStatic
    fun configuration(workWorkerFactory: WorkWorkerFactory) = Configuration.Builder()
        .setWorkerFactory(workWorkerFactory)
        .build()
}