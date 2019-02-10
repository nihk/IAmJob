package nick.notification

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.work.WorkerFactoryModule
import nick.work.WorkerKey

@Module(includes = [WorkerFactoryModule::class])
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(SomethingElse::class)
    abstract fun somethingElse(factory: SomethingElse.Factory): ChildWorkerFactory
}