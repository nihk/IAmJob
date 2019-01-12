package nick.core.util

import android.net.ConnectivityManager
import nick.core.di.AppScope
import javax.inject.Inject

@AppScope
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isConnected() = connectivityManager.activeNetworkInfo?.isConnected == true
}