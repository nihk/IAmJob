package nick.iamjob

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import nick.core.di.ApplicationInitializers
import nick.iamjob.di.DaggerApplicationComponent
import javax.inject.Inject

class IAmJobApplication : DaggerApplication() {

    @Inject
    lateinit var applicationInitializers: ApplicationInitializers

    override fun onCreate() {
        super.onCreate()
        applicationInitializers.initialize()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
}