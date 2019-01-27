package nick.iamjob.data

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchesViewModel @Inject constructor(
    private val repository: SearchesRepository
) : ViewModel() {
}