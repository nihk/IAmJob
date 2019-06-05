package nick.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Single
import nick.core.di.ApplicationScope
import nick.core.util.Event
import nick.core.util.NetworkBoundResource
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.networking.GitHubJobsService
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
class PositionsRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val positionsDao: PositionsDao
) {

    private val _noResultsFound = MutableLiveData<Event<Unit>>()
    val noResultsFound: LiveData<Event<Unit>> get() = _noResultsFound

    fun search(search: Search): Single<List<Position>> = with(search) {
        service.fetchPositions(
            description = description.ifEmpty { null },
            location = location.ifEmpty { null },
            isFullTime = isFullTime,
            page = page
        )
    }

    fun searchThenInsert(search: Search): Completable =
        search(search)
            .flatMapCompletable { fetchedPositions ->
                if (fetchedPositions.isEmpty()) {
                    Timber.d("No results found for: $search")
                    _noResultsFound.postValue(Event(Unit))
                }

                insertWhileReconcilingCachedPositions(fetchedPositions, search.isFirstPage())
            }

    private fun insertWhileReconcilingCachedPositions(positions: List<Position>, isFirstPage: Boolean): Completable =
        Completable.fromAction { positionsDao.insertWhileReconcilingCachedPositions(positions, isFirstPage) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySaved()

    fun queryFreshPositions() = positionsDao.queryFresh()

    fun queryCachedPositionsBlocking() = positionsDao.queryCachedBlocking()

    fun positionById(id: String) = positionsDao.positionById(id)

    fun searchPositions(search: Search): NetworkBoundResource<List<Position>, List<Position>> {
        return object : NetworkBoundResource<List<Position>, List<Position>>() {

            override fun loadFromDb(): LiveData<List<Position>> {
                return queryFreshPositions()
            }

            override fun createCall(): Single<List<Position>> = with(search) {
                search(search)
                    .doOnSuccess {
                        if (it.isEmpty()) {
                            Timber.d("No results found for: $search")
                            _noResultsFound.postValue(Event(Unit))
                        }
                    }
            }

            override fun saveCallResult(item: List<Position>): Completable {
                return insertWhileReconcilingCachedPositions(item, search.isFirstPage())
            }
        }
    }
}