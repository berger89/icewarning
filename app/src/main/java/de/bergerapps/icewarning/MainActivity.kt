package de.bergerapps.icewarning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.bergerapps.icewarning.ui.main.MainFragment
import de.bergerapps.icewarning.worker.MyDailyJob


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        MyDailyJob.schedule()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
