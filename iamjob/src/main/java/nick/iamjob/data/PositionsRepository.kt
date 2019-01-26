package nick.iamjob.data

import io.reactivex.Completable
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.networking.GitHubJobsService
import javax.inject.Inject

class PositionsRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val positionsDao: PositionsDao
) {

    fun search(search: Search): Completable = with(search) {
        service.fetchPositions(
            description,
            location?.description,
            location?.latitude,
            location?.longitude,
            isFullTime,
            page
        ).flatMapCompletable { fetchedPositions ->
            // Mark all fresh cached positions as stale -- we don't want them showing up in search results
            // if they're not part of the remotely fetched result set
            val cachedPositions = positionsDao.queryCachedFreshBlocking().map {
                it.copy(isFresh = false)
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
                // Add all remaining stale cached positions so they can be updated in the local db
                it.addAll(cachedPositions)
            }

            // Insert will replace (effectively updating) any positions with the same ID
            deleteAllNonCachableThenInsert(reconciledPositions)
        }
    }

    private fun deleteAllNonCachableThenInsert(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.deleteAllNonCachableThenInsert(positions) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySaved()

    fun queryFreshPositions() = positionsDao.queryFresh()
}