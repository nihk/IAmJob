package nick.iamjob.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_jobs.*
import nick.core.util.visibleOrGone
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.data.PositionsViewModel
import nick.iamjob.util.OnPositionClickedListener
import nick.iamjob.util.PositionAction
import nick.iamjob.util.PositionsLoadingState
import nick.ui.BaseFragment

class JobsFragment
    : BaseFragment()
    , OnPositionClickedListener
    , FilterPositionsDialogFragment.OnFilterDefinedListener {

    private val viewModel: PositionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val adapter = PositionsAdapter(this)

    private var currentFilter = Search.EMPTY

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                filter.hide()
            } else if (dy < 0) {
                filter.show()
            }
        }
    }

    companion object {
        const val KEY_CURRENT_FILTER = "current_filter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // fixme: process death is showing a blank screen
        if (savedInstanceState == null) {
            search(currentFilter)
        } else {
            currentFilter = savedInstanceState.getParcelable(KEY_CURRENT_FILTER) ?: Search.EMPTY
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_jobs, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    LinearLayoutManager(requireContext())
                } else {
                    GridLayoutManager(requireContext(), 2)
                }
        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(scrollListener)
        swipe_refresh.setOnRefreshListener {
            search(currentFilter,
                onLoading = PositionsLoadingState.SwipeRefreshFetch,
                onDoneLoading = PositionsLoadingState.SwipeRefreshDoneFetch
            )
        }
        filter.setOnClickListener {
            FilterPositionsDialogFragment.create()
                .show(childFragmentManager, FilterPositionsDialogFragment.TAG)
        }
        clear_filter.setOnClickListener {
            search(Search.EMPTY)
            active_filter_container.visibleOrGone(false)
        }
        setActiveFilter(currentFilter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PositionsLoadingState.SimpleFetch -> progress_bar.visibleOrGone(true)
                is PositionsLoadingState.SimpleDoneFetch -> progress_bar.visibleOrGone(false)
                is PositionsLoadingState.SwipeRefreshFetch -> Unit
                is PositionsLoadingState.SwipeRefreshDoneFetch -> swipe_refresh.isRefreshing = false
            }
            no_results_message.visibleOrGone(false)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                ErrorDialogFragment.create(it.message)
                    .show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        })

        viewModel.positions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            no_results_message.visibleOrGone(it.isEmpty())
            setActiveFilter(currentFilter)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_FILTER, currentFilter)
    }

    override fun onDestroyView() {
        recycler_view.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

    override fun onPositionClicked(positionAction: PositionAction) {
        with(positionAction) {
            when (this) {
                is PositionAction.SaveOrUnsave -> viewModel.saveOrUnsavePosition(position)
                is PositionAction.MoreDetails -> {
                    viewModel.setPositionViewed(position)
                    findNavController().navigate(
                        JobsFragmentDirections.toPosition(position)
                    )
                }
            }
        }
    }

    fun search(
        search: Search,
        onLoading: PositionsLoadingState = PositionsLoadingState.SimpleFetch,
        onDoneLoading: PositionsLoadingState = PositionsLoadingState.SimpleDoneFetch
    ) {
        currentFilter = search
        viewModel.search(currentFilter, onLoading, onDoneLoading)
    }

    override fun onFilterDefined(search: Search, saveFilter: Boolean) {
        search(search)

        if (saveFilter) {
            // todo: save filter to FiltersDao
        }
    }

    private fun setActiveFilter(search: Search) {
        active_filter_container.visibleOrGone(currentFilter != Search.EMPTY)
        val activeFilterComponents = mutableListOf(
            search.description.orEmpty(),
            search.location?.description.orEmpty(),
            if (search.isFullTime) "Full time" else ""
        ).also { it.removeAll { s -> s.isBlank() } }

        active_filter.text = activeFilterComponents.joinToString(" | ")
    }
}