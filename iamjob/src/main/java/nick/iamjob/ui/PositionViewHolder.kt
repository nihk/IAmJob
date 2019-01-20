package nick.iamjob.ui

import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_position.view.*
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.util.OnPositionClicked
import nick.iamjob.util.PositionAction

class PositionViewHolder(
    view: View,
    private val onPositionClicked: OnPositionClicked
) : RecyclerView.ViewHolder(view) {

    fun bind(position: Position) {
        with(itemView) {
            title.text = position.title
            company.text = position.company
            location.text = position.location

            @ColorInt val textColor = ContextCompat.getColor(context,
                if (position.hasViewed) {
                    R.color.hasViewedText
                } else {
                    R.color.hasNotViewedText
                }
            )

            title.setTextColor(textColor)
            company.setTextColor(textColor)
            location.setTextColor(textColor)

            save_position.isEnabled = true
            save_position.setImageResource(
                if (position.isSaved) {
                    R.drawable.ic_saved_filled
                } else {
                    R.drawable.ic_saved
                }
            )

            setOnClickListener {
                onPositionClicked.handleAction(PositionAction.MoreDetails(position))
            }

            save_position.setOnClickListener {
                onPositionClicked.handleAction(PositionAction.SaveOrUnsave(position))
                // To prevent click spamming
                save_position.isEnabled = false
            }
        }
    }
}