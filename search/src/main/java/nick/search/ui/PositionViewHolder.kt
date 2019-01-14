package nick.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_position.view.*
import nick.data.model.Position
import nick.search.R
import nick.search.util.OnPositionClicked
import nick.search.util.PositionAction

class PositionViewHolder(
    view: View,
    private val onPositionClicked: OnPositionClicked
) : RecyclerView.ViewHolder(view) {

    fun bind(position: Position) {
        itemView.title.text = position.title
        itemView.company.text = position.company
        itemView.location.text = position.location
        itemView.save_position.isEnabled = true
        itemView.save_position.setImageResource(
            if (position.isSaved) {
                R.drawable.ic_saved_filled
            } else {
                R.drawable.ic_saved
            }
        )

        itemView.setOnClickListener {
            onPositionClicked.handleAction(PositionAction.MoreDetails(position))
        }

        itemView.save_position.setOnClickListener {
            onPositionClicked.handleAction(PositionAction.SaveOrUnsave(position))
            itemView.save_position.isEnabled = false
        }
    }
}