package nick.iamjob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.Event
import nick.core.util.applySchedulers
import nick.data.model.Position
import nick.data.model.Search
import nick.iamjob.util.PositionsQuery
import nick.iamjob.util.PositionsLoadingState
import timber.log.Timber
import javax.inject.Inject

class PositionsViewModel @Inject constructor(
    private val repository: PositionsRepository
) : BaseRxViewModel() {

    private val _loadingState = MutableLiveData<PositionsLoadingState>()
    private val _error = MutableLiveData<Event<Throwable>>()
    private val positionsQuery = MutableLiveData<PositionsQuery>()

    val positions: LiveData<List<Position>> = Transformations.switchMap(positionsQuery) {
        when (it) {
            is PositionsQuery.SavedPositions -> repository.querySavedPositions()
            is PositionsQuery.FreshPositions -> repository.queryFreshPositions()
        }
    }

    val loadingState: LiveData<PositionsLoadingState> get() = _loadingState
    val error: LiveData<Event<Throwable>> get() = _error

    fun search(search: Search = Search.EMPTY) {
        repository.search(search)
            .applySchedulers()
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                    _loadingState.value = PositionsLoadingState.FetchingPositions
                }

                override fun onComplete() {
                    _loadingState.value = PositionsLoadingState.DoneFetchingPositions
                    queryPositions(PositionsQuery.FreshPositions)
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    _loadingState.value = PositionsLoadingState.DoneFetchingPositions
                    _error.value = Event(e)

                    // Display cached content if remote fetching failed
                    queryPositions(PositionsQuery.FreshPositions)
                }
            })
    }

    fun saveOrUnsavePosition(position: Position) {
        repository.updatePosition(position.copy(isSaved = position.isSaved.not()))
            .applySchedulers()
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    _error.value = Event(e)
                }
            })
    }

    fun queryPositions(positionsQuery: PositionsQuery) {
        this.positionsQuery.value = positionsQuery
    }
}