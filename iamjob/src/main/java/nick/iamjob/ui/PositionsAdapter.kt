package nick.iamjob.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.util.OnPositionActionListener

class PositionsAdapter(
    private val onPositionActionListener: OnPositionActionListener,
    private val showFadedViewedPosition: Boolean = true
) : ListAdapter<Position, PositionViewHolder>(PositionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_position, parent, false)
            .let {
                PositionViewHolder(
                    it,
                    onPositionActionListener,
                    showFadedViewedPosition
                )
            }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}