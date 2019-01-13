package nick.search.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.core.di.ViewModelKey
import nick.search.PositionsViewModel

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PositionsViewModel::class)
    abstract fun positionsViewModel(positionsViewModel: PositionsViewModel): ViewModel
}