package nick.ui

import android.widget.Toast
import dagger.android.support.DaggerFragment
import nick.core.vm.ViewModelFactory
import javax.inject.Inject

open class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    fun toast(message: String?) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
}