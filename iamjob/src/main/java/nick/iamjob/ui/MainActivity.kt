package nick.iamjob.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nick.iamjob.R

// UI should ideally look something like:
// https://blog.usejournal.com/android-material-design-components-backdrop-96a3044a3b2
// todo: use WorkManager to poll API once per day and check for new results?
// todo: filter by created date/sort by created date?
// todo: favorite searches
// todo: long press PositionViewHolder to mark as unViewed (?)
// todo: 'days ago' in PositionViewHolder. Color code for recency green -> red
// todo: notifications tab to subscribe to favorite searches
// todo: pagination
class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (supportFragmentManager.findFragmentById(R.id.i_am_job_host) as? NavHostFragment)
            ?.let {
                bottom_navigation.setupWithNavController(it.navController)
            }
    }
}
