package nick.iamjob.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nick.iamjob.R

// UI should ideally look something like:
// https://blog.usejournal.com/android-material-design-components-backdrop-96a3044a3b2
// todo: swipe to blacklist forever job
// todo: use WorkManager to poll API once per day and check for new results?
// todo: error dialog: A wild error appeared! Here's what some guy wrote to explain this issue: e.message
// todo: filter by created date/sort by created date?
// todo: favorite searches
// todo: notifications tab to subscribe to favorite searches
class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (supportFragmentManager.findFragmentById(R.id.i_am_job_host)
                as? NavHostFragment)?.let {
            bottom_navigation.setupWithNavController(it.navController)
        }
    }
}
