package nick.iamjob.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nick.iamjob.R

// UI should ideally look something like:
// https://blog.usejournal.com/android-material-design-components-backdrop-96a3044a3b2
// todo: tint status bar to be translucent
// todo: screen should draw under status bar
// todo: swipe to refresh search page
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
