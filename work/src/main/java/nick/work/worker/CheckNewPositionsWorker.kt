package nick.work.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import nick.core.util.CurrentTime
import nick.data.model.Position
import nick.repository.PositionsRepository
import nick.repository.SearchesRepository
import timber.log.Timber

class CheckNewPositionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currentTime: CurrentTime,
    private val positionsRepository: PositionsRepository,
    private val searchesRepository: SearchesRepository
) : Worker(context, workerParameters) {

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

    override fun doWork(): Result {
        val queryAllBlocking = searchesRepository.queryAllBlocking().filter {
            it.isSubscribed
        }
        queryAllBlocking.forEach {
            positionsRepository.search(it)
                .subscribe(object : SingleObserver<List<Position>> {

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onSuccess(t: List<Position>) {
                        t.forEach { position ->
                            Timber.d("${it.description} - ${it.location}: ${position.company}")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }

                })
        }
        return Result.success()
    }
}