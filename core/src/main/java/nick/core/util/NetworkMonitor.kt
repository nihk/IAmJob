package nick.core.util

import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isConnected() = connectivityManager.activeNetworkInfo?.isConnected == true
}