package nick.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import nick.data.model.EphemeralPosition
import nick.search.R
import nick.search.util.OnPositionClicked

class EphemeralPositionsAdapter(
    private val onPositionClicked: OnPositionClicked
) : ListAdapter<EphemeralPosition, EphemeralPositionViewHolder>(EphemeralPositionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ephemeral_position, parent, false)
            .let { EphemeralPositionViewHolder(it, onPositionClicked) }

    override fun onBindViewHolder(holder: EphemeralPositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}