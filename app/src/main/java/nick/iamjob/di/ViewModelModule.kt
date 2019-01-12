package nick.iamjob.di

import dagger.Module
import nick.core.di.ViewModelFactoryModule

@Module(
    includes = [
        ViewModelFactoryModule::class
    ]
)
abstract class ViewModelModule {
}