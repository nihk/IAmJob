package nick.iamjob.ui

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nick.iamjob.R

// todo: use WorkManager to poll API once per day and check for new results?
// todo: notifications tab to subscribe to favorite searches
// todo: endlessly struggle to do shared element transitions
// todo: position fragment
// todo: need to scroll to top on filter action
// todo: test coverage - compare with how android-sunflower app did its testing
class MainActivity
    : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigation.findNavController(this, R.id.i_am_job_host).let {
            bottom_navigation.setupWithNavController(it)
        }
    }
}
