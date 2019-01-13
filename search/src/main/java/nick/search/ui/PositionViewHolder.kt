package nick.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_position.view.*
import nick.data.model.Position

class PositionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // TODO: Use DataBinding if possible
    fun bind(position: Position) {
        itemView.title.text = position.title
        itemView.company.text = position.company
        itemView.location.text = position.location
    }
}