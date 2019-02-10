package nick.work.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope

@Module(includes = [WorkAssistedModule::class, WorkerModule::class])
object WorkModule {

    @ApplicationScope
    @Provides
    @JvmStatic
    fun workManager(
        @ApplicationContext applicationContext: Context,
        configuration: Configuration
    ): WorkManager {
        // todo: move this to an ApplicationInitializers class
        WorkManager.initialize(applicationContext, configuration)
        return WorkManager.getInstance()
    }

    @Provides
    @JvmStatic
    fun configuration(workWorkerFactory: WorkWorkerFactory) = Configuration.Builder()
        .setWorkerFactory(workWorkerFactory)
        .build()
}