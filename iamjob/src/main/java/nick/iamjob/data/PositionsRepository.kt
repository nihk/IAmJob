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

    fun search(search: Search): Completable = with(search) {
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

            insertWhileReconcilingCachedPositions(fetchedPositions, search.isFirstPage())
        }
    }

    private fun insertWhileReconcilingCachedPositions(positions: List<Position>, isFirstPage: Boolean): Completable =
        Completable.fromAction { positionsDao.insertWhileReconcilingCachedPositions(positions, isFirstPage) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySaved()

    fun queryFreshPositions() = positionsDao.queryFresh()

    fun positionById(id: String) = positionsDao.positionById(id)
}