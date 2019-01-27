package nick.iamjob.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_position.*
import nick.iamjob.R
import nick.ui.BaseFragment
import nick.ui.GlideApp

class PositionFragment : BaseFragment() {

    private val args by navArgs<PositionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_position, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(this)
            .load(args.position.companyLogo)
            .placeholder(R.drawable.ic_jobs)
            .into(company_logo)
    }
}