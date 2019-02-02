package nick.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Reusable
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import java.util.*

@Module
object LocationModule {

    @ApplicationScope
    @Reusable
    @JvmStatic
    fun locationServices(@ApplicationContext applicationContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

    @ApplicationScope
    @Reusable
    @JvmStatic
    fun geocoder(@ApplicationContext applicationContext: Context): Geocoder =
        Geocoder(applicationContext, Locale.getDefault())
}