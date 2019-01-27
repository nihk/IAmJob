package nick.iamjob.ui

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_position.view.*
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.util.OnPositionClickedListener
import nick.iamjob.util.PositionAction
import nick.ui.GlideApp

// TODO: Need "days ago"
class PositionViewHolder(
    view: View,
    private val onPositionClickedListener: OnPositionClickedListener
) : RecyclerView.ViewHolder(view) {

    fun bind(position: Position) {
        with(itemView) {
            title.text = position.title
            @SuppressLint("SetTextI18n")
            company.text = position.company
            location.text = position.location
            time_ago.text = DateUtils.getRelativeTimeSpanString(position.createdAt)

            GlideApp.with(itemView)
                .load(position.companyLogo)
                .placeholder(R.drawable.ic_jobs)
                .into(company_logo)

            resetTextColors(position)
            resetSaveIcon(position)

            setOnClickListener {
                onPositionClickedListener.onPositionClicked(PositionAction.MoreDetails(position))
            }

            save_position.setOnClickListener {
                onPositionClickedListener.onPositionClicked(PositionAction.SaveOrUnsave(position))
                // To prevent click spamming
                save_position.isEnabled = false
            }
        }
    }

    private fun resetTextColors(position: Position) {
        with (itemView) {
            val alpha = if (position.hasViewed) 0.4f else 1.0f

            @ColorInt val titleTextColor = ContextCompat.getColor(
                context,
                if (position.hasViewed) {
                    R.color.darkGrey
                } else {
                    android.R.color.black
                }
            )

            title.setTextColor(titleTextColor)
            title.alpha = alpha
            company.alpha = alpha
            location.alpha = alpha
            time_ago.alpha = alpha
        }
    }

    private fun resetSaveIcon(position: Position) {
        with (itemView) {
            save_position.isEnabled = true
            save_position.setImageResource(
                if (position.isSaved) {
                    R.drawable.ic_saved_filled
                } else {
                    R.drawable.ic_saved
                }
            )
        }
    }
}