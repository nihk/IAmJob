package nick.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_search.*
import nick.core.util.visibleOrGone
import nick.search.PositionViewModel
import nick.search.R
import nick.search.util.OnPositionClicked
import nick.search.util.PositionAction
import nick.search.util.PositionsLoadingState
import nick.ui.BaseFragment

// TODO: Scrolling down needs to slide search fields and bottom nav out of view
class SearchFragment
    : BaseFragment()
    , OnPositionClicked {

    private val viewModel: PositionViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionViewModel::class.java)
    }

    private val adapter = SearchResultsAdapter(this)

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
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is PositionsLoadingState.FetchingPositions -> progress_bar.visibleOrGone(true)
                is PositionsLoadingState.DoneFetchingPositions -> progress_bar.visibleOrGone(false)
                is PositionsLoadingState.SavingPosition -> Unit  // Do nothing
                is PositionsLoadingState.DoneSavingPosition -> Toast.makeText(requireContext(), "Saved 'em", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                // TODO: UI error state, not just a Toast
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                    .show()
            }
        })

        viewModel.savedPositions.observe(viewLifecycleOwner, Observer {
            // TODO: positions list needs to reflect these 'saved' states
        })

        viewModel.positions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun handleAction(positionAction: PositionAction) {
        with(positionAction) {
            when (this) {
                is PositionAction.Save -> viewModel.savePosition(position, doSave)
                is PositionAction.MoreDetails -> {
                    findNavController().navigate(SearchFragmentDirections.toPosition(position))
                }
            }
        }
    }
}