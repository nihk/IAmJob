package nick.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_saved_position.view.*
import nick.data.model.SavedPosition
import nick.search.util.OnPositionClicked
import nick.search.util.PositionAction

class SavedPositionViewHolder(
    view: View,
    private val onPositionClicked: OnPositionClicked
) : RecyclerView.ViewHolder(view) {

    fun bind(savedPosition: SavedPosition) {
        itemView.title.text = savedPosition.title
        itemView.setOnClickListener {
            onPositionClicked.handleAction(PositionAction.MoreDetails(TODO()))
        }
    }
}