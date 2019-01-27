package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.net.ConnectivityManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import nick.data.di.DataModule
import nick.networking.RemoteDataModule
import java.util.*

@Module(
    includes = [
        ViewModelModule::class,
        RemoteDataModule::class,
        DataModule::class
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

    @ApplicationScope
    @Provides
    fun locationServices(@ApplicationContext applicationContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

    @ApplicationScope
    @Provides
    fun geocoder(@ApplicationContext applicationContext: Context): Geocoder =
            Geocoder(applicationContext, Locale.getDefault())
}