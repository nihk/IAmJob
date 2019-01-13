package nick.core.util

import android.net.ConnectivityManager
import nick.core.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isConnected() = connectivityManager.activeNetworkInfo?.isConnected == true
}