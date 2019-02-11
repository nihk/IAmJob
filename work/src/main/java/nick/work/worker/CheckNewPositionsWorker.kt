package nick.work.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import nick.data.model.Position
import nick.data.model.Search
import nick.repository.PositionsRepository
import nick.repository.SearchesRepository
import timber.log.Timber

class CheckNewPositionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val positionsRepository: PositionsRepository,
    private val searchesRepository: SearchesRepository
) : Worker(context, workerParameters) {

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

    override fun doWork(): Result {
        val subscribedSearches = searchesRepository.queryAllBlocking()
            .filter(Search::isSubscribed)

        val toUpdate = mutableListOf<Search>()

        subscribedSearches.forEach { search ->
            val positions: List<Position> = positionsRepository.search(search)
                .onErrorReturnItem(emptyList())
                .blockingGet()

            val numNewResults = positions.filter {
                    position ->  position.createdAt > search.lastTimeUserSearched
            }.size

            if (numNewResults > 0) {
                toUpdate.add(search.copy(numNewResults = numNewResults))
            }
        }

        Timber.d("Found new results for ${toUpdate.size} saved filters")
        searchesRepository.updateBlocking(toUpdate)

        // todo: if toUpdate.size > 0, then create a notification that deep links to NotificationFragment

        return Result.success()
    }
}