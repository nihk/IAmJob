package nick.work.worker

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
import nick.core.di.ApplicationContext
import nick.work.R
import javax.inject.Inject

class NewPositionsNotifier @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    fun postDeepLinkNotification(
        @DrawableRes smallIcon: Int,
        @NavigationRes navigationRes: Int,
        @IdRes destination: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                applicationContext.getString(R.string.channel_id),
                applicationContext.getString(R.string.channel_name),
                importance
            )
            channel.description = applicationContext.getString(R.string.channel_description)

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(navigationRes)
            .setDestination(destination)
            .createPendingIntent()

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.channel_id))
                .setSmallIcon(smallIcon)
                .setContentTitle(applicationContext.getString(R.string.notification_title))
                .setContentText(applicationContext.getString(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext)
            .notify(applicationContext.resources.getInteger(R.integer.notification_id), builder.build())
    }
}