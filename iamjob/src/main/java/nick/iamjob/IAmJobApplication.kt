package nick.iamjob

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import nick.core.di.Initializer
import nick.iamjob.di.DaggerApplicationComponent
import javax.inject.Inject

class IAmJobApplication : DaggerApplication() {

    @Inject
    lateinit var initializers: Set<@JvmSuppressWildcards Initializer>

    override fun onCreate() {
        super.onCreate()
        initializers.forEach(Initializer::initialize)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
}