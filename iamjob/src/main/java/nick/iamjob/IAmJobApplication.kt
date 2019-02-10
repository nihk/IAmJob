package nick.iamjob

import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import nick.iamjob.di.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class IAmJobApplication : DaggerApplication() {

    @Inject
    lateinit var configuration: Configuration

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        WorkManager.initialize(this, configuration)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
}