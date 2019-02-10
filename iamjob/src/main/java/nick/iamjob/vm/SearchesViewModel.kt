package nick.iamjob.vm

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
import nick.repository.SearchesRepository
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

    fun update(search: Search) {
        repository.update(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }

    fun updateLastTimeUserSearched(search: Search) {
        repository.updateLastTimeUserSearched(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }

    fun insertOrUpdate(search: Search) {
        repository.insertOrUpdateLastTimeUserSearched(search)
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

    fun setNotificationFrequency(notificationFrequency: Int) {
        repository.setNotificationFrequency(notificationFrequency)
    }

    fun getNotificationFrequency() = repository.getNotificationFrequency()
}