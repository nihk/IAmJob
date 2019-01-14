package nick.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.Event
import nick.core.util.applySchedulers
import nick.data.model.EphemeralPosition
import nick.data.model.SavedPosition
import nick.data.model.Search
import nick.search.util.PositionsLoadingState
import timber.log.Timber
import javax.inject.Inject

class PositionsViewModel @Inject constructor(
    private val repository: PositionsRepository
) : BaseRxViewModel() {

    private val _loadingState = MutableLiveData<PositionsLoadingState>()
    private val _error = MutableLiveData<Event<Throwable>>()

    val ephemeralPositions = repository.positions
    val savedPositions = repository.savedPositions
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
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    _loadingState.value = PositionsLoadingState.DoneFetchingPositions
                    _error.value = Event(e)
                }
            })
    }

    fun saveOrUnsavePosition(ephemeralPosition: EphemeralPosition) {
        val completable = if (ephemeralPosition.isSaved) {
            repository.deleteSavedPosition(ephemeralPosition.id)
        } else {
            val savedPosition =
                SavedPosition(ephemeralPosition.copy(isSaved = ephemeralPosition.isSaved.not()))
            repository.insertSavedPosition(savedPosition)
        }

        completable
            .andThen(repository.updateEphemeralPosition(ephemeralPosition.copy(isSaved = ephemeralPosition.isSaved.not())))
            .applySchedulers()
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                    _loadingState.value = PositionsLoadingState.SavingOrUnsavingPosition
                }

                override fun onComplete() {
                    _loadingState.value = PositionsLoadingState.DoneSavingOrUnsavingPosition
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    _loadingState.value = PositionsLoadingState.DoneSavingOrUnsavingPosition
                    _error.value = Event(e)
                }
            })
    }
}