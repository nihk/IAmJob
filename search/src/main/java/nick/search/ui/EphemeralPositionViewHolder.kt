package nick.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ephemeral_position.view.*
import nick.data.model.EphemeralPosition
import nick.search.R
import nick.search.util.OnPositionClicked
import nick.search.util.PositionAction

class EphemeralPositionViewHolder(
    view: View,
    private val onPositionClicked: OnPositionClicked
) : RecyclerView.ViewHolder(view) {

    fun bind(ephemeralPosition: EphemeralPosition) {
        itemView.title.text = ephemeralPosition.title
        itemView.company.text = ephemeralPosition.company
        itemView.location.text = ephemeralPosition.location
        setUiSavedState(ephemeralPosition)
        itemView.save_position.isEnabled = true

        itemView.setOnClickListener {
            onPositionClicked.handleAction(PositionAction.MoreDetails(ephemeralPosition))
        }

        itemView.save_position.setOnClickListener {
            onPositionClicked.handleAction(PositionAction.SaveOrUnsave(ephemeralPosition))
            itemView.save_position.isEnabled = false
        }
    }

    private fun setUiSavedState(ephemeralPosition: EphemeralPosition) {
        itemView.save_position.setImageResource(
            if (ephemeralPosition.isSaved) {
                R.drawable.ic_saved_filled
            } else {
                R.drawable.ic_saved
            }
        )
    }
}