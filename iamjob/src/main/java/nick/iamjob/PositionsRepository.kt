package nick.iamjob

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
            // Mark all saved positions as stale -- we don't want them showing up in search results
            // if they're not part of the remotely fetched result set
            val savedPositions = positionsDao.querySavedBlocking().map {
                it.copy(isFresh = false)
            }

            // Apply saved position states to the newly fetched positions
            val reconciledPositions = fetchedPositions.toMutableList().map { fetchedPosition ->
                val foundSavedPosition: Position? = savedPositions.find { savedPosition ->
                    savedPosition.id == fetchedPosition.id
                }

                fetchedPosition.copy(
                    isSaved = foundSavedPosition?.isSaved == true,
                    hasApplied = foundSavedPosition?.hasApplied == true,
                    isFresh = true
                )
            }

            // This will also update any saved rows that were marked as isFresh == true
            deleteAllUnsavedThenInsert(reconciledPositions)
        }
    }

    private fun deleteAllUnsavedThenInsert(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.deleteAllUnsavedThenInsert(positions) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySaved()

    fun queryFreshPositions() = positionsDao.queryFresh()
}