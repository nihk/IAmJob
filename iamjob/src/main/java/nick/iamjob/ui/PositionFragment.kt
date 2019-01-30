package nick.iamjob.ui

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.format.DateUtils
import android.text.method.MovementMethod
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_position.*
import kotlinx.android.synthetic.main.position_header.*
import nick.core.util.visibleOrGone
import nick.data.model.Position
import nick.iamjob.R
import nick.iamjob.data.PositionsViewModel
import nick.ui.BaseFragment
import nick.ui.GlideApp
import nick.ui.HtmlWrapper
import timber.log.Timber
import javax.inject.Inject

class PositionFragment : BaseFragment() {

    private val args by navArgs<PositionFragmentArgs>()

    @Inject
    lateinit var htmlWrapper: HtmlWrapper

    @Inject
    lateinit var movementMethod: MovementMethod

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PositionsViewModel::class.java)
    }

    private val listener by lazy {
        requireContext() as ToolbarSetter
    }

    private val white by lazy {
        ContextCompat.getColor(requireContext(), android.R.color.white)
    }

    private var currentPositionState: Position? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_position, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(args.position) {
            toolbar_layout.title = title
            position_title.text = title
            position_description.text = htmlWrapper.fromHtml(description).trim()
            position_location.text = location
            val companyDetails = if (companyUrl != null) {
                resources.getString(R.string.company_details, companyUrl, company)
                    .let { htmlWrapper.fromHtml(it) }
            } else {
                company
            }
            position_company_details.text = companyDetails
            position_company_details.movementMethod = movementMethod
            posted_date.text = DateUtils.getRelativeTimeSpanString(createdAt)
            if (howToApply.isNullOrBlank()) {
                how_to_apply.visibleOrGone(false)
                divider.visibleOrGone(false)
            } else {
                how_to_apply.text = htmlWrapper.fromHtml(howToApply!!).trim()
                how_to_apply.movementMethod = movementMethod
            }

            GlideApp.with(this@PositionFragment)
                .load(companyLogo)
                .placeholder(R.drawable.ic_jobs)
                .into(company_logo)
        }

        listener.setToolbar(toolbar, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.positionById(args.position.id).observe(viewLifecycleOwner, Observer {
            currentPositionState = it
            Timber.d("Got updated Position from database. isSaved: ${it.isSaved}")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_position, menu)
        doHackToChangeMenuIconColors(menu)
        menu.findItem(R.id.toggle_save)?.let {
            setPositionSavedUiState(it, args.position.isSaved)
        }
    }

    // Workaround for having a NoActionBar theme
    private fun doHackToChangeMenuIconColors(menu: Menu) {
        for (i in 0 until menu.size()) {
            menu.getItem(i).icon?.let {
                setDrawableWhite(it)
            }
        }
    }

    private fun setDrawableWhite(drawable: Drawable) {
        drawable.mutate()
        drawable.setColorFilter(
            white,
            PorterDuff.Mode.SRC_ATOP
        )
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            requireActivity().onBackPressed()
            true
        }
        R.id.share -> false
        R.id.toggle_save -> {
            // Only update the DB if the UI and model states are synched
            if (currentPositionState?.isSaved == item.isChecked) {
                setPositionSavedUiState(item, !item.isChecked)
                viewModel.saveOrUnsavePosition(currentPositionState!!)
                Timber.d("Toggling position saved state in database")
            } else {
                Timber.d("Currently in process of saving position, aborting toggle logic")
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setPositionSavedUiState(toggleSave: MenuItem, isSaved: Boolean) {
        toggleSave.icon = ContextCompat.getDrawable(
            requireContext(),
            if (isSaved) {
                R.drawable.ic_saved_filled
            } else {
                R.drawable.ic_saved
            }
        )?.also { setDrawableWhite(it) }
        toggleSave.isChecked = isSaved
    }
}