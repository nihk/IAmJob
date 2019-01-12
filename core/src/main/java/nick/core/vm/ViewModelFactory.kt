package nick.core.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nick.core.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel? = creators[modelClass]?.get()
        return requireNotNull(modelClass.cast(viewModel))
    }
}