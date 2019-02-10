package nick.work.di

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import nick.work.di.WorkWorkerFactory

@Module
abstract class WorkerFactoryModule {

    @Binds
    abstract fun workerFactory(factory: WorkWorkerFactory): WorkerFactory
}