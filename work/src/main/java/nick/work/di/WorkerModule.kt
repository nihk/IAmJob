package nick.work.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.work.worker.CheckNewPositionsWorker
import nick.work.worker.ChildWorkerFactory

@Module(includes = [WorkerFactoryModule::class])
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CheckNewPositionsWorker::class)
    abstract fun checkNewPositionsWorker(factory: CheckNewPositionsWorker.Factory): ChildWorkerFactory
}