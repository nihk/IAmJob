package nick.iamjob.data

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.applySchedulers
import nick.data.model.Search
import timber.log.Timber
import javax.inject.Inject

class SearchesViewModel @Inject constructor(
    private val repository: SearchesRepository,
    private val geocoder: Geocoder
) : BaseRxViewModel() {

    val searches = repository.searches
    private val _locality = MutableLiveData<String>()
    val locality: LiveData<String> get() = _locality

    fun insert(search: Search) {
        repository.insert(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }

    fun delete(search: Search) {
        repository.delete(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }

    private fun createSimpleCompletableObserver(): CompletableObserver =
        object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                addDisposable(d)
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
            }
        }

    fun fetchLocation(location: Task<Location>) {
        location.addOnCompleteListener { task ->
            _locality.value = task.result?.let { location ->
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    .firstOrNull()
                    ?.locality
            }
        }
    }

    fun updateSearch(search: Search) {
        repository.updateSearch(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }
}