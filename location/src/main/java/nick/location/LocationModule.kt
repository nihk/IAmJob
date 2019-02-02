package nick.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.Reusable
import nick.core.di.ApplicationContext
import java.util.*

@Module
object LocationModule {

    @Reusable
    @Provides
    @JvmStatic
    fun locationServices(@ApplicationContext applicationContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

    @Reusable
    @Provides
    @JvmStatic
    fun geocoder(@ApplicationContext applicationContext: Context): Geocoder =
        Geocoder(applicationContext, Locale.getDefault())
}