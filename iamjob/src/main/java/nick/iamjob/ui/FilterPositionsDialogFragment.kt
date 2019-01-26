package nick.iamjob.ui

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import dagger.android.support.DaggerDialogFragment
import nick.data.model.Search
import nick.iamjob.R

class FilterPositionsDialogFragment : DaggerDialogFragment() {

    private val listener by lazy {
        parentFragment as OnFilterDefinedListener
    }

    interface OnFilterDefinedListener {
        fun onFilterDefined(search: Search)
    }

    companion object {
        val TAG: String = FilterPositionsDialogFragment::class.java.simpleName

        fun create() = FilterPositionsDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {
            title(text = "Do something")
            positiveButton(R.string.apply_fiter) {
                listener.onFilterDefined(Search.EMPTY)
            }
        }
}