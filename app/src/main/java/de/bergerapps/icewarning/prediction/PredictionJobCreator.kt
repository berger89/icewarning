package de.bergerapps.icewarning.prediction

import androidx.annotation.Nullable
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


class PredictionJobCreator : JobCreator {
    @Nullable
    override fun create(tag: String): Job? {
        return when (tag) {
            PredictionDailyJob.TAG -> PredictionDailyJob()
            else -> null
        }
    }
}