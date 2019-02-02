package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import nick.data.di.DataModule
import nick.location.LocationModule
import nick.networking.RemoteDataModule
import nick.ui.UiModule

@Module(
    includes = [
        ViewModelModule::class,
        RemoteDataModule::class,
        DataModule::class,
        UiModule::class,
        LocationModule::class
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

    @ApplicationScope
    @Provides
    fun connectivityManager(@ApplicationContext applicationContext: Context) =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}