package nick.work.worker

import androidx.lifecycle.LiveData
import androidx.work.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val NAME_CHECK_NEW_RESULTS_WORK = "check_new_results_work"

class CheckNewPositionsEnqueuer @Inject constructor(
    private val workManager: WorkManager
) {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun enqueueWork(daysInterval: Long, cancelPreviousWork: Boolean = false) {
        Timber.d("Enqueuing work for daysInterval: $daysInterval")

        if (cancelPreviousWork) {
            workManager.cancelUniqueWork(NAME_CHECK_NEW_RESULTS_WORK)
        }

        workManager.enqueueUniquePeriodicWork(
            NAME_CHECK_NEW_RESULTS_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<CheckNewPositionsWorker>(daysInterval, TimeUnit.DAYS/*, 5, TimeUnit.MINUTES*/)
                .setConstraints(constraints)
                .build()
        )
    }

    fun cancelWork(): LiveData<Operation.State> = workManager.cancelUniqueWork(
        NAME_CHECK_NEW_RESULTS_WORK
    ).state
}