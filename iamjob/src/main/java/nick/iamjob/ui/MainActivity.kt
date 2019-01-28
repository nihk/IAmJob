package nick.iamjob.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nick.iamjob.R
import nick.iamjob.util.LocationClient
import nick.iamjob.util.LocationServicesProvider
import javax.inject.Inject

// todo: use WorkManager to poll API once per day and check for new results?
// todo: notifications tab to subscribe to favorite searches
// todo: endlessly struggle to do shared element transitions
// todo: position fragment
// todo: need to scroll to top on filter action
// todo: test coverage - compare with how android-sunflower app did its testing
class MainActivity
    : DaggerAppCompatActivity()
    , LocationServicesProvider {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationClient: LocationClient? = null

    companion object {
        const val REQUEST_LOCATION = 7  // For good luck
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigation.findNavController(this, R.id.i_am_job_host).let {
            bottom_navigation.setupWithNavController(it)
        }
    }

    override fun requestLocation(client: LocationClient) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION
            )
            locationClient = client
        } else {
            messageLocationClient(client)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                locationClient?.let {
                    if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                        messageLocationClient(it)
                    }
                }

                locationClient = null
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun messageLocationClient(client: LocationClient) {
        client.onLocationTaskReceived(fusedLocationProviderClient.lastLocation)
    }
}
