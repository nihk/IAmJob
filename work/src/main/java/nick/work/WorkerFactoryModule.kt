package nick.work

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module

@Module
abstract class WorkerFactoryModule {

    @Binds
    abstract fun workerFactory(factory: WorkWorkerFactory): WorkerFactory
}