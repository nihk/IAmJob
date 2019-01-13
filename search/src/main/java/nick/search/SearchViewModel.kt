package nick.search

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.applySchedulers
import nick.data.model.Position
import nick.data.model.Search
import javax.inject.Inject

// TODO: Add local storage
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
): BaseRxViewModel() {

    fun search(search: Search) {
        repository.search(search)
            .applySchedulers()
            .subscribe(object : SingleObserver<List<Position>> {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onSuccess(positions: List<Position>) {
                }

                override fun onError(e: Throwable) {
                }
            })
    }
}