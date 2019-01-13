package nick.search.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.core.di.ViewModelKey
import nick.search.PositionViewModel

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PositionViewModel::class)
    abstract fun positionViewModel(positionViewModel: PositionViewModel): ViewModel
}