package nick.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_saved_positions.*
import nick.core.util.visibleOrGone
import nick.search.PositionsViewModel
import nick.search.R
import nick.search.util.OnPositionClicked
import nick.search.util.PositionAction
import nick.ui.BaseFragment

class SavedPositionsFragment
    : BaseFragment()
    , OnPositionClicked {

    private val viewModel: PositionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val adapter = SavedPositionsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved_positions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.savedPositions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            progress_bar.visibleOrGone(false)
        })
    }

    override fun handleAction(positionAction: PositionAction) {
        with (positionAction) {
            when (this) {
                is PositionAction.MoreDetails -> findNavController().navigate(
                    SavedPositionsFragmentDirections.toPosition(position)
                )
            }
        }
    }
}