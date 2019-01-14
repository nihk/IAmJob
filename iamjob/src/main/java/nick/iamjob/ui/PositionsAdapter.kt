package nick.iamjob.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.util.OnPositionClicked

class PositionsAdapter(
    private val onPositionClicked: OnPositionClicked
) : ListAdapter<Position, PositionViewHolder>(PositionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_position, parent, false)
            .let { PositionViewHolder(it, onPositionClicked) }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}