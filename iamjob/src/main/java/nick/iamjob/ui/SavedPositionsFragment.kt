package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_saved_positions.*
import nick.data.model.Position
import nick.data.util.PositionQuery
import nick.iamjob.R
import nick.iamjob.data.PositionsViewModel
import nick.iamjob.util.OnPositionActionListener
import nick.iamjob.util.PositionAction
import nick.ui.BaseFragment
import nick.ui.visibleOrGone

class SavedPositionsFragment
    : BaseFragment()
    , OnPositionActionListener {

    private val viewModel: PositionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val adapter = PositionsAdapter(this, false)

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

        viewModel.positions.observe(
            viewLifecycleOwner,
            Observer { savedPositions: List<Position>? ->
                adapter.submitList(savedPositions)
                nothing_saved_container.visibleOrGone(savedPositions.isNullOrEmpty())
                progress_bar.visibleOrGone(false)
            })

        viewModel.queryPositions(PositionQuery.SavedPositions)
    }

    override fun onPositionAction(positionAction: PositionAction) {
        with(positionAction) {
            when (this) {
                is PositionAction.SaveOrUnsave -> viewModel.saveOrUnsavePosition(position)
                is PositionAction.MoreDetails -> {
                    viewModel.setPositionViewed(position)
                    findNavController().navigate(
                        SavedPositionsFragmentDirections.toPosition(position)
                    )
                }
            }
        }
    }
}