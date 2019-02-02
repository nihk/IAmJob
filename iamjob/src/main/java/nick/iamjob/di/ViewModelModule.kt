package nick.iamjob.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.core.di.ViewModelFactoryModule
import nick.core.di.ViewModelKey
import nick.iamjob.vm.PositionsViewModel
import nick.iamjob.vm.SearchesViewModel

@Module(
    includes = [
        ViewModelFactoryModule::class
    ]
)
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PositionsViewModel::class)
    abstract fun positionsViewModel(positionsViewModel: PositionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchesViewModel::class)
    abstract fun searchesViewModel(searchesViewModel: SearchesViewModel): ViewModel
}