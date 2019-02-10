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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_notification_filter.*
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.vm.SearchesViewModel
import nick.ui.BaseFragment
import nick.ui.visibleOrGone
import nick.work.CheckNewPositionsWorker
import javax.inject.Inject

class NotificationsFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
    }

    private val adapter = FilterAdapter()

    private var setAdapter = false

    private val workManager by lazy { WorkManager.getInstance() }

    @Inject
    lateinit var wm: WorkManager

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

        notification_frequency.setOnClickListener {
            wm.beginUniqueWork(
                "asdf",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<CheckNewPositionsWorker>().build()
            ).enqueue()
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