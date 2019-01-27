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
import nick.iamjob.data.SearchesViewModel
import nick.iamjob.util.OnPositionClickedListener
import nick.iamjob.util.PositionAction
import nick.iamjob.util.PositionsLoadingState
import nick.iamjob.util.PositionsQuery
import nick.ui.BaseFragment
import javax.inject.Inject

class JobsFragment
    : BaseFragment()
    , OnPositionClickedListener
    , FilterPositionsDialogFragment.OnFilterDefinedListener {

    @Inject
    lateinit var filterStringFormatter: FilterStringFormatter

    private val positionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }
    private val searchesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
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

        positionsViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PositionsLoadingState.SimpleFetch -> progress_bar.visibleOrGone(true)
                is PositionsLoadingState.SimpleDoneFetch -> progress_bar.visibleOrGone(false)
                is PositionsLoadingState.SwipeRefreshFetch -> Unit
                is PositionsLoadingState.SwipeRefreshDoneFetch -> swipe_refresh.isRefreshing = false
            }
            no_results_message.visibleOrGone(false)
        })

        positionsViewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                ErrorDialogFragment.create(it.message)
                    .show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        })

        positionsViewModel.positions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            no_results_message.visibleOrGone(it.isEmpty())
            setActiveFilter(currentFilter)
        })

        if (savedInstanceState != null) {
            positionsViewModel.queryPositions(PositionsQuery.FreshPositions)
        }
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
                is PositionAction.SaveOrUnsave -> positionsViewModel.saveOrUnsavePosition(position)
                is PositionAction.MoreDetails -> {
                    positionsViewModel.setPositionViewed(position)
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
        positionsViewModel.search(currentFilter, onLoading, onDoneLoading)
    }

    override fun onFilterDefined(search: Search, saveFilter: Boolean) {
        search(search)

        if (saveFilter && search != Search.EMPTY) {
            searchesViewModel.insert(search)
        }
    }

    private fun setActiveFilter(search: Search) {
        active_filter_container.visibleOrGone(currentFilter != Search.EMPTY)
        active_filter.text = filterStringFormatter.format(search)
    }
}