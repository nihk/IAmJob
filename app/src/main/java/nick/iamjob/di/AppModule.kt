package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import nick.core.di.AppScope
import nick.networking.di.RemoteDataModule

@Module(
    includes = [
        ViewModelModule::class,
        RemoteDataModule::class
    ]
)
class AppModule {

    @AppScope
    @Provides
    fun applicationContext(application: Application): Context = application.applicationContext

    @AppScope
    @Provides
    fun applicationResources(application: Application): Resources = application.resources

    @AppScope
    @Provides
    fun connectivityManager(applicationContext: Context) =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}