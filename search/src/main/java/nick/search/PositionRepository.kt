package nick.search

import io.reactivex.Completable
import nick.data.dao.PositionsDao
import nick.data.dao.SavedPositionsDao
import nick.data.model.Position
import nick.data.model.SavedPosition
import nick.data.model.Search
import nick.networking.service.GitHubJobsService
import javax.inject.Inject

class PositionRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val positionsDao: PositionsDao,
    private val savedPositionsDao: SavedPositionsDao
) {

    val positions = positionsDao.queryAll()
    val savedPositions = savedPositionsDao.queryAll()

    fun search(search: Search): Completable = with(search) {
        service.fetchPositions(
            description,
            location?.description,
            location?.latitude,
            location?.longitude,
            isFullTime,
            page
        ).flatMapCompletable {
            deleteAllPositions()
                .andThen(insertPositions(it))
        }
    }

    fun deleteAllPositions(): Completable =
        Completable.fromAction { positionsDao.deleteAll() }

    fun insertPositions(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.insertEntities(positions) }

    fun insertSavedPosition(savedPosition: SavedPosition): Completable =
        Completable.fromAction { savedPositionsDao.insertEntity(savedPosition) }

    fun deleteSavedPosition(savedPosition: SavedPosition): Completable =
        Completable.fromAction { savedPositionsDao.deleteEntity(savedPosition) }
}