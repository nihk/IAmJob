package nick.work.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import nick.core.di.ApplicationContext
import nick.core.di.Initializer
import javax.inject.Inject

class WorkManagerInitializer @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val configuration: Configuration
): Initializer {

    override fun initialize() {
        WorkManager.initialize(applicationContext, configuration)
    }
}