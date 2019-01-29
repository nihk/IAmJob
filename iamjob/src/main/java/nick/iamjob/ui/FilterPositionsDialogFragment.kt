package nick.iamjob.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_filter_positions_dialog.*
import kotlinx.android.synthetic.main.item_saved_filter.view.*
import nick.core.util.visibleOrGone
import nick.core.vm.ViewModelFactory
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.data.SearchesViewModel
import javax.inject.Inject

class FilterPositionsDialogFragment
    : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var filterStringFormatter: FilterStringFormatter

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val searchesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
    }

    private val listener by lazy {
        parentFragment as OnFilterDefinedListener
    }

    private val adapter by lazy { SavedFiltersAdapter() }

    interface OnFilterDefinedListener {
        fun onFilterDefined(search: Search, saveFilterLocally: Boolean)
    }

    companion object {
        val TAG: String = FilterPositionsDialogFragment::class.java.simpleName
        private const val REQUEST_LOCATION = 7  // For good luck

        fun create() = FilterPositionsDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialDialog(requireContext()).show {

            customView(R.layout.fragment_filter_positions_dialog, scrollable = true)
            positiveButton(R.string.apply_fiter) { dialog ->
                with(dialog) {
                    listener.onFilterDefined(
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
            requestLocationPermission()
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

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            fetchLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLastLocation() {
        searchesViewModel.fetchLocation(fusedLocationProviderClient.lastLocation)
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
                    listener.onFilterDefined(search, false)
                    dialog?.dismiss()
                }
            }

        }
    }
}