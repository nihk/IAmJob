package nick.search.ui

import androidx.lifecycle.ViewModelProviders
import nick.search.SearchViewModel
import nick.ui.BaseFragment

class SearchResultsFragment : BaseFragment() {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }
}