package nick.iamjob.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
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

            if (currentFilter.arePagesExhausted()
                || positionsViewModel.loadingState.value is PositionsLoadingState.Loading
            ) {
                return
            }

            val layoutManager = recycler_view.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            currentFilter = positionsViewModel.maybePaginate(
                currentFilter,
                visibleItemCount,
                lastVisibleItem,
                totalItemCount
            )
        }
    }

    private val onBackPressedCallback = OnBackPressedCallback {
        if (!currentFilter.isEmpty()) {
            onFilterDefined(Search.EMPTY, false)
            true
        } else {
            false
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
            currentFilter = currentFilter.toFirstPage()
            search(
                currentFilter,
                onLoading = PositionsLoadingState.SwipeRefreshing,
                onDoneLoading = PositionsLoadingState.DoneSwipeRefreshing
            )
        }
        filter.setOnClickListener {
            FilterPositionsDialogFragment.create()
                .show(childFragmentManager, FilterPositionsDialogFragment.TAG)
        }
        clear_filter.setOnClickListener {
            onFilterDefined(Search.EMPTY, false)
        }
        setUiActiveFilter(currentFilter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        positionsViewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PositionsLoadingState.SimpleFetch -> progress_bar.visibleOrGone(true)
                is PositionsLoadingState.DoneSimpleFetch -> progress_bar.visibleOrGone(false)
                is PositionsLoadingState.SwipeRefreshing -> Unit
                is PositionsLoadingState.DoneSwipeRefreshing -> swipe_refresh.isRefreshing = false
                is PositionsLoadingState.Paginating -> Unit
                is PositionsLoadingState.DonePaginating -> Unit
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
        })

        positionsViewModel.exhaustedPages.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                currentFilter = currentFilter.toExhausted()
            }
        })

        if (savedInstanceState != null) {
            positionsViewModel.queryPositions(PositionsQuery.FreshPositions)
        }

        val activity: FragmentActivity = requireActivity()
        activity.addOnBackPressedCallback(onBackPressedCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_FILTER, currentFilter)
    }

    override fun onDestroyView() {
        recycler_view.removeOnScrollListener(scrollListener)
        val activity: FragmentActivity = requireActivity()
        activity.removeOnBackPressedCallback(onBackPressedCallback)
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
        onDoneLoading: PositionsLoadingState = PositionsLoadingState.DoneSimpleFetch
    ) {
        positionsViewModel.search(search, onLoading, onDoneLoading)
    }

    override fun onFilterDefined(search: Search, saveFilterLocally: Boolean) {
        currentFilter = search
        setUiActiveFilter(currentFilter)
        search(currentFilter)

        if (saveFilterLocally && !search.isEmpty()) {
            searchesViewModel.insert(search)
        }
    }

    private fun setUiActiveFilter(search: Search) {
        active_filter_container.visibleOrGone(!currentFilter.isEmpty())
        active_filter.text = filterStringFormatter.format(search)
    }
}