package nick.networking

import android.net.ConnectivityManager
import dagger.Reusable
import javax.inject.Inject

@Reusable
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isConnected() = connectivityManager.activeNetworkInfo?.isConnected == true
}