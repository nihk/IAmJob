package nick.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import nick.core.util.CurrentTime
import timber.log.Timber

class CheckNewPositionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currentTime: CurrentTime
) : Worker(context, workerParameters) {

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

    override fun doWork(): Result {
        Timber.d(currentTime.inMillis().toString())
        return Result.success()
    }
}