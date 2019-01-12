package nick.core.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import nick.core.vm.ViewModelFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}