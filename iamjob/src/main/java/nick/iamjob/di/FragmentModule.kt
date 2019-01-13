package nick.iamjob.di

import dagger.Module
import nick.search.di.SearchUiModule

@Module(
    includes = [
        SearchUiModule::class
    ]
)
abstract class FragmentModule