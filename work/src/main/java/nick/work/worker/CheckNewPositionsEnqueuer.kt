package nick.work.worker

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.lifecycle.LiveData
import androidx.work.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val NAME_CHECK_NEW_RESULTS_WORK = "check_new_results_work"

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
        @IdRes destinationId: Int,
        @ColorRes color: Int,
        replaceExistingWork: Boolean
    ) {
        Timber.d("Maybe enqueuing work for daysInterval: $daysInterval")

        val inputData = Data.Builder()
            .putInt(CheckNewPositionsWorker.KEY_SMALL_ICON, smallIcon)
            .putInt(CheckNewPositionsWorker.KEY_NAVIGATION_RES, navigationRes)
            .putInt(CheckNewPositionsWorker.KEY_DESTINATION_ID, destinationId)
            .putInt(CheckNewPositionsWorker.KEY_COLOR, color)
            .build()

        val existingPeriodicWorkPolicy = if (replaceExistingWork) {
            ExistingPeriodicWorkPolicy.REPLACE
        } else {
            ExistingPeriodicWorkPolicy.KEEP
        }

        workManager.enqueueUniquePeriodicWork(
            NAME_CHECK_NEW_RESULTS_WORK,
            existingPeriodicWorkPolicy,
            PeriodicWorkRequestBuilder<CheckNewPositionsWorker>(daysInterval, TimeUnit.DAYS)
                .setInitialDelay(daysInterval, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
        )
    }

    fun cancelWork(): LiveData<Operation.State> = workManager.cancelUniqueWork(
        NAME_CHECK_NEW_RESULTS_WORK
    ).state
}