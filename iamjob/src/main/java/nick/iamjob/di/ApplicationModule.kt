package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationScope
import nick.core.di.ApplicationContext
import nick.networking.di.RemoteDataModule

@Module(
    includes = [
        ViewModelModule::class,
        RemoteDataModule::class
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