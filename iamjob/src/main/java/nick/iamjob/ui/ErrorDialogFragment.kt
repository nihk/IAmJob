package nick.iamjob.ui

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import dagger.android.support.DaggerDialogFragment
import nick.iamjob.R

class ErrorDialogFragment : DaggerDialogFragment() {

    private val message by lazy {
        arguments?.getString(KEY_MESSAGE)
    }

    companion object {
        val TAG: String = ErrorDialogFragment::class.java.simpleName
        private const val KEY_MESSAGE = "message"

        fun create(message: String?) = ErrorDialogFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_MESSAGE, message)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {
            title(text = getString(R.string.a_wild_error_appeared))
            positiveButton(android.R.string.ok)
            message(text = getString(R.string.something_went_wrong, message))
            icon(R.drawable.ic_error)
        }
}