package nick.iamjob

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import nick.iamjob.di.DaggerApplicationComponent
import timber.log.Timber

class IAmJobApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
}