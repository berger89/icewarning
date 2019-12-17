package de.bergerapps.icewarning

import android.app.Application
import com.evernote.android.job.JobManager
import de.bergerapps.icewarning.worker.DemoJobCreator


class ForecastApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(DemoJobCreator())
    }

}