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
import nick.work.R

object NotificationUtil {

    fun postDeepLinkNotification(
        context: Context,
        @DrawableRes smallIcon: Int,
        @NavigationRes navigationRes: Int,
        @IdRes destination: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                context.getString(R.string.channel_id),
                context.getString(R.string.channel_name),
                importance
            )
            channel.description = context.getString(R.string.channel_description)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(navigationRes)
            .setDestination(destination)
            .createPendingIntent()

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                .setSmallIcon(smallIcon)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(LongArray(0))
                .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(context)
            .notify(context.resources.getInteger(R.integer.notification_id), builder.build())
    }
}