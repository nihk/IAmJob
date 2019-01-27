package nick.iamjob.util

import android.location.Location
import com.google.android.gms.tasks.Task

interface LocationClient {

    fun onLocationTaskReceived(location: Task<Location>)
}