package de.bergerapps.icewarning

import android.app.Application
import com.evernote.android.job.JobManager
import de.bergerapps.icewarning.prediction.PredictionJobCreator


class ForecastApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(PredictionJobCreator())
    }

}