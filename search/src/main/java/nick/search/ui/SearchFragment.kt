package nick.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_search.*
import nick.data.model.Location
import nick.data.model.Search
import nick.search.R
import nick.ui.BaseFragment

class SearchFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        execute_search.setOnClickListener {
            findNavController().navigate(toSearchResults())
        }
    }

    private fun toSearchResults(): SearchFragmentDirections.ToSearchResults {
        return SearchFragmentDirections.toSearchResults(
            Search(
                description = description_input.text.toString(),
                location = Location(description = location_input.text.toString()),
                isFullTime = full_time_only.isChecked
            )
        )
    }
}