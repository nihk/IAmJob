package nick.notification

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object NotificationModule {

    @Reusable
    @Provides
    @JvmStatic
    fun workManager() = WorkManager.getInstance()
}