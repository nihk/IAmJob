package nick.ui

import dagger.android.support.DaggerFragment
import nick.core.vm.ViewModelFactory
import javax.inject.Inject

open class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}