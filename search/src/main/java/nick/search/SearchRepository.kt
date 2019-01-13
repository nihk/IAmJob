package nick.search

import io.reactivex.Completable
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.networking.service.GitHubJobsService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val dao: PositionsDao
) {

    val positions = dao.queryAll()

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
        Completable.fromAction { dao.deleteAll() }

    fun insertPositions(positions: List<Position>): Completable =
        Completable.fromAction { dao.insertEntities(positions) }
}