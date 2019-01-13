package nick.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import nick.data.model.SavedPosition
import nick.search.R
import nick.search.util.OnPositionClicked

class SavedPositionsAdapter(
    private val onPositionClicked: OnPositionClicked
) : ListAdapter<SavedPosition, SavedPositionViewHolder>(SavedPositionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_position, parent, false)
            .let { SavedPositionViewHolder(it, onPositionClicked) }

    override fun onBindViewHolder(holder: SavedPositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}