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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_jobs.*
import nick.core.util.Resource
import nick.data.model.Position
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.util.OnPositionActionListener
import nick.iamjob.util.PositionAction
import nick.iamjob.vm.PositionsViewModel
import nick.iamjob.vm.SearchesViewModel
import nick.ui.BaseFragment
import nick.ui.ErrorDialogFragment
import nick.ui.visibleOrGone
import javax.inject.Inject

class JobsFragment
    : BaseFragment()
    , OnPositionActionListener
    , FilterPositionsDialogFragment.OnFilterDefinedListener {

    private val args by navArgs<JobsFragmentArgs>()

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

    private var wasFabHiddenDuringDestroy = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                filter.hide()
            } else if (dy < 0) {
                filter.show()
            }

            if (currentFilter.arePagesExhausted()
                || positionsViewModel.positions.value is Resource.Loading
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

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (!currentFilter.isEmpty()) {
                onFilterDefined(Search.EMPTY, false)
            }
        }
    }

    companion object {
        const val KEY_CURRENT_FILTER = "current_filter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // Hack workaround for JobsFragment not always having non-null arguments
            currentFilter = try {
                args.search!!
            } catch (throwable: Throwable) {
                Search.EMPTY
            }
        } else {
            currentFilter = savedInstanceState.getParcelable(KEY_CURRENT_FILTER) ?: Search.EMPTY
        }

        searchThenUpdate(currentFilter)
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
            searchThenUpdate(currentFilter)
        }
        if (wasFabHiddenDuringDestroy) {
            filter.hide()
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


        positionsViewModel.positions.observe(viewLifecycleOwner, Observer { resource: Resource<List<Position>> ->
            when (resource) {
                is Resource.Loading -> {
                    swipe_refresh.isRefreshing = true
                    resource.data?.let(adapter::submitList)
                }
                is Resource.Success -> with(resource) {
                    onPositionsFound(data)
                    no_results_message.visibleOrGone(data.isNullOrEmpty())
                }
                is Resource.Failure -> with(resource) {
                    onPositionsFound(data)

                    if (childFragmentManager.findFragmentByTag(ErrorDialogFragment.TAG) == null) {
                        ErrorDialogFragment.create(throwable.message)
                            .show(childFragmentManager, ErrorDialogFragment.TAG)
                    }
                }
            }
        })

        positionsViewModel.noResultsFound.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                currentFilter = currentFilter.toExhausted()
            }
        })

        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    private fun onPositionsFound(positions: List<Position>?) {
        swipe_refresh.isRefreshing = false
        adapter.submitList(positions)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_FILTER, currentFilter)
    }

    override fun onDestroyView() {
        recycler_view.removeOnScrollListener(scrollListener)
        onBackPressedCallback.isEnabled = false
        wasFabHiddenDuringDestroy = filter.isOrWillBeHidden
        super.onDestroyView()
    }

    override fun onPositionAction(positionAction: PositionAction) {
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

    fun search(search: Search) {
        positionsViewModel.setSearch(search)
    }

    fun searchThenUpdate(
        search: Search,
        saveFilterLocally: Boolean = false
    ) {
        search(search)

        if (!search.isEmpty()) {
            if (saveFilterLocally) {
                searchesViewModel.insertOrUpdate(search)
            } else {
                searchesViewModel.updateLastTimeUserSearched(search)
            }
        }
    }

    override fun onFilterDefined(search: Search, saveFilterLocally: Boolean) {
        currentFilter = search
        setUiActiveFilter(currentFilter)
        searchThenUpdate(search, saveFilterLocally = saveFilterLocally)
    }

    private fun setUiActiveFilter(search: Search) {
        active_filter_container.visibleOrGone(!currentFilter.isEmpty())
        onBackPressedCallback.isEnabled = !currentFilter.isEmpty()
        active_filter.text = filterStringFormatter.format(search)
    }
}