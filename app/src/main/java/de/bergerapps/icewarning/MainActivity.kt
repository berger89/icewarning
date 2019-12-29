package de.bergerapps.icewarning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import de.bergerapps.icewarning.prediction.PredictionDailyJob
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bttm_nav.setupWithNavController(navController)

        PredictionDailyJob.schedule(applicationContext)
    }

}
