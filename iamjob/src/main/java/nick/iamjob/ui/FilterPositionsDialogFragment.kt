package nick.iamjob.ui

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import dagger.android.support.DaggerDialogFragment

class FilterPositionsDialogFragment : DaggerDialogFragment() {

    companion object {
        private val TAG: String = FilterPositionsDialogFragment::class.java.simpleName

        fun create() = FilterPositionsDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {
            // TODO
        }
}