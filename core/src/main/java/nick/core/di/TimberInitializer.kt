package nick.core.di

import nick.core.BuildConfig
import nick.core.util.GlobalTagTree
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor() : Initializer {

    override fun initialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(GlobalTagTree())
        }
    }
}