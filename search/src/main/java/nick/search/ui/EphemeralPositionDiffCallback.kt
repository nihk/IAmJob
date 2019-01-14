package nick.search.ui

import androidx.recyclerview.widget.DiffUtil
import nick.data.model.EphemeralPosition

object EphemeralPositionDiffCallback : DiffUtil.ItemCallback<EphemeralPosition>() {

    override fun areItemsTheSame(oldItem: EphemeralPosition, newItem: EphemeralPosition) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EphemeralPosition, newItem: EphemeralPosition) =
        oldItem == newItem
}