package nick.iamjob.data

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.applySchedulers
import nick.data.model.Search
import timber.log.Timber
import javax.inject.Inject

class SearchesViewModel @Inject constructor(
    private val repository: SearchesRepository
) : BaseRxViewModel() {

    val searches = repository.searches

    fun insert(search: Search) {
        repository.insert(search)
            .applySchedulers()
            .subscribe(createSimpleCompletableObserver())
    }

    fun deleteSearch(search: Search) {
        repository.deleteSearch(search)
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
}