package nick.iamjob.ui

import androidx.recyclerview.widget.DiffUtil
import nick.data.model.Position

object PositionDiffCallback : DiffUtil.ItemCallback<Position>() {

    override fun areItemsTheSame(oldItem: Position, newItem: Position) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Position, newItem: Position) =
        oldItem == newItem
}