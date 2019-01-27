package nick.iamjob.ui

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.gms.tasks.Task
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_filter_positions_dialog.*
import kotlinx.android.synthetic.main.item_saved_filter.view.*
import nick.core.util.visibleOrGone
import nick.core.vm.ViewModelFactory
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.data.SearchesViewModel
import nick.iamjob.util.LocationClient
import nick.iamjob.util.LocationServicesProvider
import javax.inject.Inject

class FilterPositionsDialogFragment
    : DaggerDialogFragment()
    , LocationClient {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filterStringFormatter: FilterStringFormatter

    private val searchesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
    }

    private val parentFragmentListener by lazy {
        parentFragment as OnFilterDefinedListener
    }

    private val activityListener by lazy {
        activity as LocationServicesProvider
    }

    private val adapter by lazy { SavedFiltersAdapter() }

    interface OnFilterDefinedListener {
        fun onFilterDefined(search: Search, saveFilterLocally: Boolean)
    }

    companion object {
        val TAG: String = FilterPositionsDialogFragment::class.java.simpleName

        fun create() = FilterPositionsDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {

            customView(R.layout.fragment_filter_positions_dialog, scrollable = true)
            positiveButton(R.string.apply_fiter) { dialog ->
                with(dialog) {
                    parentFragmentListener.onFilterDefined(
                        Search(
                            description = description.text.toString(),
                            location = location.text.toString(),
                            isFullTime = full_time.isChecked
                        ), save_filter.isChecked
                    )
                }
            }
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.use_my_location?.setOnClickListener {
            activityListener.requestLocation(this)
            // To prevent spam clicks
            it.isEnabled = false
        }

        // Can't seem to use viewLifecycleOwner here - it crashes the app at runtime.
        searchesViewModel.searches.observe(this, Observer { savedFilters ->
            with(dialog?.saved_filters ?: return@Observer) {
                if (savedFilters.isEmpty()) {
                    visibleOrGone(false)
                } else {
                    visibleOrGone(true)
                    if (adapter == null) {
                        adapter = this@FilterPositionsDialogFragment.adapter
                    }
                }
            }

            adapter.submitList(savedFilters)
        })

        searchesViewModel.locality.observe(this, Observer { locality: String? ->
            dialog?.use_my_location?.isEnabled = true
            if (locality != null) {
                dialog?.location?.setText(locality)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Couldn't get your last known location, sorry :(",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchesViewModel.searches.removeObservers(this)
    }

    override fun onLocationTaskReceived(location: Task<Location>) {
        searchesViewModel.fetchLocation(location)
    }

    inner class SavedFiltersAdapter :
        ListAdapter<Search, SavedFilterViewHolder>(savedFiltersDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_saved_filter, parent, false)
                .let { SavedFilterViewHolder(it) }

        override fun onBindViewHolder(holder: SavedFilterViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    val savedFiltersDiffCallback = object : DiffUtil.ItemCallback<Search>() {

        override fun areItemsTheSame(oldItem: Search, newItem: Search) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Search, newItem: Search) =
            oldItem == newItem
    }

    inner class SavedFilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(search: Search) {
            with(itemView) {
                filter_description.text = filterStringFormatter.format(search)
                remove_filter.isEnabled = true
                remove_filter.setOnClickListener {
                    // Prevent click spam
                    remove_filter.isEnabled = false
                    searchesViewModel.deleteSearch(search)
                }

                setOnClickListener {
                    parentFragmentListener.onFilterDefined(search, false)
                    dialog?.dismiss()
                }
            }

        }
    }
}