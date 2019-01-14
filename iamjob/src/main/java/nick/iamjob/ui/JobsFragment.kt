package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_jobs.*
import nick.core.util.visibleOrGone
import nick.iamjob.PositionsViewModel
import nick.iamjob.R
import nick.iamjob.util.OnPositionClicked
import nick.iamjob.util.PositionAction
import nick.iamjob.util.PositionsLoadingState
import nick.ui.BaseFragment

class JobsFragment
    : BaseFragment()
    , OnPositionClicked {

    private val viewModel: PositionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val adapter = PositionsAdapter(this)

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                filter.hide()
            } else if (dy < 0) {
                filter.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.search()
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
        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(scrollListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PositionsLoadingState.FetchingPositions -> progress_bar.visibleOrGone(true)
                is PositionsLoadingState.DoneFetchingPositions -> progress_bar.visibleOrGone(false)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                // TODO: UI error state, not just a Toast. Snackbar?
                toast(it.message)
            }
        })

        viewModel.positions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        recycler_view.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

    override fun handleAction(positionAction: PositionAction) {
        with(positionAction) {
            when (this) {
                is PositionAction.SaveOrUnsave -> viewModel.saveOrUnsavePosition(position)
                is PositionAction.MoreDetails -> findNavController().navigate(
                    JobsFragmentDirections.toPosition(position)
                )
            }
        }
    }
}