package nick.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.Event
import nick.core.util.applySchedulers
import nick.data.model.Search
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : BaseRxViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Event<Throwable>>()

    val positions = repository.positions
    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<Event<Throwable>> get() = _error

    fun search(search: Search = Search.EMPTY) {
        repository.search(search)
            .applySchedulers()
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                    _isLoading.value = true
                }

                override fun onComplete() {
                    _isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    _isLoading.value = false
                    _error.value = Event(e)
                }
            })
    }
}