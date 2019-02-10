package nick.work

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.work.ChildWorkerFactory
import nick.work.WorkerKey

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CheckNewPositionsWorker::class)
    abstract fun checkNewPositionsWorker(factory: CheckNewPositionsWorker.Factory): ChildWorkerFactory
}