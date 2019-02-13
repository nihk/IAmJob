package nick.iamjob.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import nick.core.di.Initializer
import nick.core.di.TimberInitializer
import nick.work.di.WorkManagerInitializer

@Module
abstract class InitializerModule {

    @Binds
    @IntoSet
    abstract fun timberInitializer(timberInitializer: TimberInitializer): Initializer

    @Binds
    @IntoSet
    abstract fun workManagerInitializer(workManagerInitializer: WorkManagerInitializer): Initializer
}