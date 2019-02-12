package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Operation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_notification_filter.*
import nick.data.model.Search
import nick.iamjob.R
import nick.iamjob.vm.SearchesViewModel
import nick.ui.BaseFragment
import nick.ui.DefaultAdapterItemSelectedListener
import nick.ui.visibleOrGone
import nick.work.worker.CheckNewPositionsEnqueuer
import timber.log.Timber
import javax.inject.Inject

class NotificationsFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchesViewModel::class.java)
    }

    private val adapter = FilterAdapter()

    @Inject
    lateinit var checkNewPositionsEnqueuer: CheckNewPositionsEnqueuer

    private var ignoreFirstTime = true

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
        notification_interval_spinner.onItemSelectedListener =
            object : DefaultAdapterItemSelectedListener() {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // This callback will get hit on initialization. Ignore it the first time.
                    if (ignoreFirstTime) {
                        ignoreFirstTime = false
                        return
                    }
                    viewModel.setNotificationFrequency(position)
                    val operationState = checkNewPositionsEnqueuer.cancelWork()
                    operationState.observe(viewLifecycleOwner, Observer {
                        if (it is Operation.State.SUCCESS) {
                            Timber.d("Successfully cancelled work")
                            enqueueWork(position)
                        }
                    })
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

                adapter.submitList(it)
            }
        })

        // Start the periodic work immediately as the default - daily. If the user has no
        // subscriptions, this is just a no-op.
        enqueueWork(0)
    }

    private fun enqueueWork(position: Int) {
        checkNewPositionsEnqueuer.enqueueWork(getDaysInterval(position))
    }

    private fun getDaysInterval(position: Int) = when (position) {
        0 -> 1L
        1 -> 7L
        2 -> 30L
        else -> error("Unknown position: $position")
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

    inner class FilterViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView)
        , LayoutContainer {

        // Cached as field so listeners don't hold onto the original, potentially stale Search
        // state that came via bind(Search)
        private var search: Search? = null

        fun bind(search: Search) {
            this.search = search

            with(search()) {
                filter_description.text = description.also {
                    filter_description.visibleOrGone(it.isNotBlank())
                }
                filter_full_time.visibleOrGone(isFullTime)
                filter_location.text = location.also {
                    filter_location.visibleOrGone(it.isNotBlank())
                }
                toggle_notification.isChecked = isSubscribed

                new_content_container.visibleOrGone(numNewResults > 0)
                new_content_container.setOnClickListener {
                    findNavController().navigate(
                        NotificationsFragmentDirections.toJobs().setSearch(search())
                    )
                }
                new_content.text = getString(R.string.new_results, numNewResults)
            }

            filter_container.setOnClickListener {
                toggle_notification.toggle()
            }

            toggle_notification.setOnCheckedChangeListener { _, isChecked ->
                // Workaround for this listener firing twice for some unknown reason
                // See: https://stackoverflow.com/questions/3913687/checkbox-changes-value-twice
                toggle_notification.setOnCheckedChangeListener(null)
                viewModel.update(search().copy(isSubscribed = isChecked))
            }
        }

        private fun search(): Search = requireNotNull(search)
    }
}