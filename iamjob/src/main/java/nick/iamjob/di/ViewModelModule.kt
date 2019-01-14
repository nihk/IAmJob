package nick.iamjob.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nick.core.di.ViewModelFactoryModule
import nick.core.di.ViewModelKey
import nick.iamjob.data.PositionsViewModel

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
}