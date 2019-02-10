package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import nick.data.di.DataModule
import nick.location.LocationModule
import nick.networking.NetworkingModule
import nick.notification.NotificationModule
import nick.ui.UiModule

@Module(
    includes = [
        ViewModelModule::class,
        NetworkingModule::class,
        DataModule::class,
        UiModule::class,
        LocationModule::class,
        NotificationModule::class
    ]
)
class ApplicationModule {

    @ApplicationScope
    @Provides
    @ApplicationContext
    fun applicationContext(application: Application): Context = application.applicationContext

    @ApplicationScope
    @Provides
    fun applicationResources(application: Application): Resources = application.resources
}