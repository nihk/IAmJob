package nick.search.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.core.di.ViewModelKey
import nick.search.SearchViewModel

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun searchViewModel(searchViewModel: SearchViewModel): ViewModel
}