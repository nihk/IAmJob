package nick.iamjob.di

import dagger.Module
import nick.core.di.ViewModelFactoryModule
import nick.search.di.SearchViewModelModule

@Module(
    includes = [
        ViewModelFactoryModule::class,
        SearchViewModelModule::class
    ]
)
abstract class ViewModelModule