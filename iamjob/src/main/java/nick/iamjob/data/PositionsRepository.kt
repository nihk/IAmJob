package nick.iamjob.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import nick.core.di.ApplicationScope
import nick.core.util.Event
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.iamjob.util.PositionsLoadingState
import nick.networking.GitHubJobsService
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
class PositionsRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val positionsDao: PositionsDao
) {

    private val _exhausedPages = MutableLiveData<Event<Unit>>()
    val exhaustedPages: LiveData<Event<Unit>> get() = _exhausedPages

    fun search(search: Search, loadingState: PositionsLoadingState): Completable = with(search) {
        service.fetchPositions(
            description = description.ifEmpty { null },
            location = location.ifEmpty { null },
            isFullTime = isFullTime,
            page = page
        ).flatMapCompletable { fetchedPositions ->

            if (fetchedPositions.isEmpty()) {
                Timber.d("Exhausted pages for: $search")
                _exhausedPages.postValue(Event(Unit))
            }

            val cachedPositions = positionsDao.queryCachedBlocking().map {
                if (loadingState is PositionsLoadingState.Paginating) {
                    it
                } else {
                    // Mark all cached positions as stale -- we don't want them showing up in search results
                    // if they're not part of the remotely fetched result set
                    it.copy(isFresh = false)
                }
            }.toMutableList()

            // Apply cached position states to the newly fetched positions
            val reconciledPositions = fetchedPositions.map { fetchedPosition ->
                val cachedPosition = cachedPositions.find { position ->
                    position.id == fetchedPosition.id
                }?.also { cachedPositions.remove(it) }

                fetchedPosition.copy(
                    isSaved = cachedPosition?.isSaved == true,
                    hasApplied = cachedPosition?.hasApplied == true,
                    hasViewed = cachedPosition?.hasViewed == true,
                    isFresh = true
                )
            }.toMutableList().also {
                // Add all remaining stale cached positions so they can be updated in the local db as such
                it.addAll(cachedPositions)
            }

            if (loadingState is PositionsLoadingState.Paginating) {
                insert(reconciledPositions)
            } else {
                // Insert will replace (effectively updating) any positions with the same ID
                deleteAllNonCachableThenInsert(reconciledPositions)
            }
        }
    }

    private fun insert(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.insert(positions) }

    private fun deleteAllNonCachableThenInsert(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.deleteAllNonCachableThenInsert(positions) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySaved()

    fun queryFreshPositions() = positionsDao.queryFresh()

    fun positionById(id: String) = positionsDao.positionById(id)
}