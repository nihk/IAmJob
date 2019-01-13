package nick.search

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
}