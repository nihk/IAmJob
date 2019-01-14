package nick.search

import io.reactivex.Completable
import nick.data.dao.EphemeralPositionsDao
import nick.data.dao.SavedPositionsDao
import nick.data.model.EphemeralPosition
import nick.data.model.SavedPosition
import nick.data.model.Search
import nick.networking.service.GitHubJobsService
import javax.inject.Inject

// FIXME: New idea -- only have one Position POJO. It will have a field for a timestamp of when it was fetched.
// only show jobs in the SearchFragment if it matches that timestamp. If any of the previously saved positions
// are found from the remote call, update their timestamp
// On new items, only delete those that aren't saved
class PositionsRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val ephemeralPositionsDao: EphemeralPositionsDao,
    private val savedPositionsDao: SavedPositionsDao
) {

    val positions = ephemeralPositionsDao.queryAll()
    val savedPositions = savedPositionsDao.queryAll()

    fun search(search: Search): Completable = with(search) {
        service.fetchPositions(
            description,
            location?.description,
            location?.latitude,
            location?.longitude,
            isFullTime,
            page
        ).flatMapCompletable { fetchedPositions ->

            val savedPositions = savedPositionsDao.queryAllBlocking().toMutableList()

            // Update saved positions with any changes that happened remotely
            savedPositions.forEachIndexed { index, savedPosition ->
                fetchedPositions.find {
                    savedPosition.id == it.id
                }?.let {
                    savedPositions.set(index, SavedPosition(it, savedPosition.isSaved, savedPosition.hasApplied))
                }
            }

            // Now convert the fetched positions into ephemeral positions that reflect saved position states
            val ephemeralPositions = fetchedPositions.toMutableList()
                .map { fetchedPosition ->
                    val savedPosition: SavedPosition? = savedPositions.find { savedPosition ->
                        savedPosition.id == fetchedPosition.id
                    }

                    EphemeralPosition(
                        fetchedPosition,
                        isSaved = savedPosition?.isSaved == true,
                        hasApplied = savedPosition?.hasApplied == true
                    )
                }


            // fetch all saved positions, then map incoming data to PositionUiState if possible, delete everything(?), then insert
            deleteAllEphemeralPositions()
                .andThen(insertEphemeralPositions(ephemeralPositions))
        }
    }

    fun deleteAllEphemeralPositions(): Completable =
        Completable.fromAction { ephemeralPositionsDao.deleteAll() }

    fun insertEphemeralPositions(ephemeralPositions: List<EphemeralPosition>): Completable =
        Completable.fromAction { ephemeralPositionsDao.insert(ephemeralPositions) }

    fun insertSavedPosition(savedPosition: SavedPosition): Completable =
        Completable.fromAction { savedPositionsDao.insert(savedPosition) }

    fun deleteSavedPosition(id: String): Completable =
        Completable.fromAction { savedPositionsDao.deleteById(id) }

    fun updateEphemeralPosition(ephemeralPosition: EphemeralPosition): Completable =
        Completable.fromAction { ephemeralPositionsDao.update(ephemeralPosition) }
}