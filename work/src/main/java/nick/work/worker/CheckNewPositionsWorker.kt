package nick.work.worker

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import timber.log.Timber

class CheckNewPositionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val searchUpdater: SearchUpdater,
    private val newPositionsNotifier: NewPositionsNotifier
) : Worker(context, workerParameters) {

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

    companion object {
        const val KEY_NAVIGATION_RES = "navigation_graph"
        const val KEY_DESTINATION_ID = "destination_id"
        const val KEY_SMALL_ICON = "small_icon"
    }

    override fun doWork(): Result {
        Timber.d("Starting work to check for new positions")

        val toUpdate = searchUpdater.findSearchesNeedingUpdating()

        if (toUpdate.isNotEmpty()) {
            Timber.d("${toUpdate.size} saved filters were found to have new results")
            searchUpdater.updateSearches(toUpdate)
            with(inputData) {
                @DrawableRes val smallIcon = getInt(KEY_SMALL_ICON, -1)
                @NavigationRes val navigationRes = getInt(KEY_NAVIGATION_RES, -1)
                @IdRes val destinationId = getInt(KEY_DESTINATION_ID, -1)

                if (smallIcon == -1 || navigationRes == -1 || destinationId == -1) {
                    error("Resource ids weren't set properly")
                } else {
                    postDeepLinkNotification(smallIcon, navigationRes, destinationId)
                }
            }
        }

        return Result.success()
    }

    private fun postDeepLinkNotification(
        @DrawableRes smallIcon: Int,
        @NavigationRes navigationRes: Int,
        @IdRes destination: Int
    ) {
        newPositionsNotifier.postDeepLinkNotification(
            smallIcon,
            navigationRes,
            destination
        )
    }
}