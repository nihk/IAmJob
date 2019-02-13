package nick.work.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder

// fixme: these constants aren't very generic
const val CHANNEL_NAME = "New positions"
const val CHANNEL_DESCRIPTION =
    "Results for your subscriptions that have appeared since your last search"
const val CHANNEL_ID = "new_positions"
const val NOTIFICATION_ID = 0x666
const val NOTIFICATION_TITLE = "New positions!"
const val NOTIFICATION_MESSAGE = "Looks like some new positions have been posted."

object NotificationUtil {

    fun postDeepLinkNotification(
        context: Context,
        @DrawableRes smallIcon: Int,
        @NavigationRes navigationRes: Int,
        @IdRes destination: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = CHANNEL_DESCRIPTION

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(navigationRes)
            .setDestination(destination)
            .createPendingIntent()

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(context)
            .notify(NOTIFICATION_ID, builder.build())
    }
}