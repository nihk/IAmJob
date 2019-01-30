package nick.iamjob.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.format.DateUtils
import android.text.method.MovementMethod
import android.view.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_position.*
import kotlinx.android.synthetic.main.position_header.*
import nick.core.util.visibleOrGone
import nick.iamjob.R
import nick.ui.BaseFragment
import nick.ui.GlideApp
import nick.ui.HtmlWrapper
import javax.inject.Inject

class PositionFragment : BaseFragment() {

    private val args by navArgs<PositionFragmentArgs>()

    @Inject
    lateinit var htmlWrapper: HtmlWrapper

    @Inject
    lateinit var movementMethod: MovementMethod

    private val listener by lazy {
        requireContext() as ToolbarSetter
    }

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

        // todo: how to apply, etc.

        with(args.position) {
            toolbar_layout.title = title
            position_title.text = title
            position_description.text = htmlWrapper.fromHtml(description).trim()
            position_location.text = location
            val rawCompanyDetails = resources.getString(R.string.company_details, companyUrl, company)
            position_company_details.text = htmlWrapper.fromHtml(rawCompanyDetails)
            position_company_details.movementMethod = movementMethod
            posted_date.text = DateUtils.getRelativeTimeSpanString(createdAt)
            if (howToApply.isNullOrBlank()) {
                how_to_apply.visibleOrGone(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_position, menu)
        doHackToChangeMenuIconColors(menu)
    }

    // Workaround for having a NoActionBar theme
    private fun doHackToChangeMenuIconColors(menu: Menu) {
        for (i in 0 until menu.size()) {
            menu.getItem(i).icon?.let {
                it.mutate()
                it.setColorFilter(
                    ContextCompat.getColor(requireContext(), android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            requireActivity().onBackPressed()
            true
        }
        R.id.share -> false
        R.id.toggle_save -> false
        else -> super.onOptionsItemSelected(item)
    }
}