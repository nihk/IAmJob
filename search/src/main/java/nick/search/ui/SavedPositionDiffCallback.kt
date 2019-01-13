package nick.search.ui

import androidx.recyclerview.widget.DiffUtil
import nick.data.model.SavedPosition

object SavedPositionDiffCallback : DiffUtil.ItemCallback<SavedPosition>() {

    override fun areItemsTheSame(oldItem: SavedPosition, newItem: SavedPosition) =
        oldItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: SavedPosition, newItem: SavedPosition) =
        oldItem == newItem
}