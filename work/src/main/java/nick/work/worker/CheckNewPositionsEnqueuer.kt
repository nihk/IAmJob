package nick.work.worker

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.lifecycle.LiveData
import androidx.work.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val NAME_CHECK_NEW_RESULTS_WORK = "check_new_results_work"
private const val FLEX_TIME_INTERVAL = 5L

class CheckNewPositionsEnqueuer @Inject constructor(
    private val workManager: WorkManager
) {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun enqueueWork(
        daysInterval: Long,
        @DrawableRes smallIcon: Int,
        @NavigationRes navigationRes: Int,
        @IdRes destinationId: Int
    ) {
        Timber.d("Maybe enqueuing work for daysInterval: $daysInterval")

        val inputData = Data.Builder()
            .putInt(CheckNewPositionsWorker.KEY_SMALL_ICON, smallIcon)
            .putInt(CheckNewPositionsWorker.KEY_NAVIGATION_RES, navigationRes)
            .putInt(CheckNewPositionsWorker.KEY_DESTINATION_ID, destinationId)
            .build()

        workManager.enqueueUniquePeriodicWork(
            NAME_CHECK_NEW_RESULTS_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<CheckNewPositionsWorker>(daysInterval, TimeUnit.DAYS, FLEX_TIME_INTERVAL, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
        )
    }

    fun cancelWork(): LiveData<Operation.State> = workManager.cancelUniqueWork(
        NAME_CHECK_NEW_RESULTS_WORK
    ).state
}