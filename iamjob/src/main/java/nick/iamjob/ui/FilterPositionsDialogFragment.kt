package nick.iamjob.ui

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_filter_positions_dialog.*
import nick.data.model.Search
import nick.iamjob.R

class FilterPositionsDialogFragment : DaggerDialogFragment() {

    private val listener by lazy {
        parentFragment as OnFilterDefinedListener
    }

    interface OnFilterDefinedListener {
        fun onFilterDefined(search: Search, saveFilter: Boolean)
    }

    companion object {
        val TAG: String = FilterPositionsDialogFragment::class.java.simpleName

        fun create() = FilterPositionsDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {
            customView(R.layout.fragment_filter_positions_dialog)
            positiveButton(R.string.apply_fiter) { dialog ->
                with(dialog) {
                    listener.onFilterDefined(Search(
                        description = description.text.toString(),
                        location = location.text.toString(),
                        isFullTime = full_time.isChecked
                    ), save_filter.isChecked)
                }
            }
        }
}