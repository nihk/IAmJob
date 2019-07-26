package nick.iamjob.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import nick.core.util.BaseRxViewModel
import nick.core.util.NetworkBoundResource
import nick.core.util.Resource
import nick.core.util.applySchedulers
import nick.data.model.Position
import nick.data.model.Search
import nick.repository.PositionsRepository
import timber.log.Timber
import javax.inject.Inject

class PositionsViewModel @Inject constructor(
    private val repository: PositionsRepository
) : BaseRxViewModel() {

    private val searchLiveData = MutableLiveData<Search>()
    private var networkBoundResource: NetworkBoundResource<List<Position>, List<Position>>? = null
    val positions: LiveData<Resource<List<Position>>> = Transformations.switchMap(searchLiveData) {
        searchPositions(it)
    }
    val savedPositions = repository.querySavedPositions()
    val noResultsFound = repository.noResultsFound
    var shouldScrollToTop = true

    private fun searchPositions(search: Search): LiveData<Resource<List<Position>>> {
        networkBoundResource?.cancel()
        return repository.searchPositions(search).let {
            networkBoundResource = it
            it.asLiveData()
        }
    }

    fun setSearch(search: Search) {
        this.searchLiveData.value = search
    }

    fun maybePaginate(
        search: Search,
        visibleItemCount: Int,
        lastVisibleItem: Int,
        totalItemCount: Int,
        itemsFromEndThreshold: Int = 10
    ): Search {
        return if (visibleItemCount + lastVisibleItem + itemsFromEndThreshold >= totalItemCount) {
            val nextPage = search.copy(page = search.page + 1)
            Timber.d("Paginating $nextPage")
            setSearch(nextPage)
            shouldScrollToTop = false
            nextPage
        } else {
            search
        }
    }

    fun saveOrUnsavePosition(position: Position) {
        subscribeToUpdateAction(repository.updatePosition(position.copy(isSaved = position.isSaved.not())))
    }

    fun setPositionViewed(position: Position) {
        if (position.hasViewed) {
            // Already viewed; no need to update anything
            return
        }

        subscribeToUpdateAction(repository.updatePosition(position.copy(hasViewed = true)))
    }

    private fun subscribeToUpdateAction(completable: Completable) {
        completable.applySchedulers()
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }
            })
    }

    fun positionById(id: String) = repository.positionById(id)
}