package nick.iamjob.ui

import androidx.recyclerview.widget.DiffUtil
import nick.data.model.Search

object FiltersDiffCallback : DiffUtil.ItemCallback<Search>() {

    override fun areItemsTheSame(oldItem: Search, newItem: Search) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Search, newItem: Search) =
        oldItem == newItem
}