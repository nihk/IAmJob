package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_notification_filter.*
import nick.core.util.visibleOrGone
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.data.SearchesViewModel
import nick.ui.BaseFragment

class NotificationsFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
    }

    private val adapter = FilterAdapter()

    private var setAdapter = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
        notification_interval_spinner.setSelection(viewModel.getNotificationFrequency())
        notification_interval_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setNotificationFrequency(position)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.searches.observe(viewLifecycleOwner, object : Observer<List<Search>> {
            override fun onChanged(it: List<Search>) {
                if (it.isEmpty()) {
                    return
                }

                no_filters_group.visibleOrGone(false)
                filters_group.visibleOrGone(true)

                // Only set the adapter once so no item animations happen.
                if (!setAdapter) {
                    setAdapter = true
                    adapter.submitList(it)
                }
            }
        })
    }

    inner class FilterAdapter
        : ListAdapter<Search, FilterViewHolder>(FiltersDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_filter, parent, false)
                .let { FilterViewHolder(it) }

        override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    inner class FilterViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView)
        , LayoutContainer {

        fun bind(search: Search) {
            with(search) {
                filter_description.text = description.also {
                    filter_description.visibleOrGone(it.isNotBlank())
                }
                filter_full_time.visibleOrGone(isFullTime)
                filter_location.text = location.also {
                    filter_location.visibleOrGone(it.isNotBlank())
                }
                toggle_notification.isChecked = isSubscribed
            }

            containerView.setOnClickListener {
                toggle_notification.toggle()
            }

            toggle_notification.setOnCheckedChangeListener { _, isChecked ->
                viewModel.update(search.copy(isSubscribed = isChecked))
            }
        }
    }
}