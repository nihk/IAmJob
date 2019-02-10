package nick.work

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [WorkerFactoryModule::class])
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CheckNewPositionsWorker::class)
    abstract fun checkNewPositionsWorker(factory: CheckNewPositionsWorker.Factory): ChildWorkerFactory
}