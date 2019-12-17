package de.bergerapps.icewarning.worker

import androidx.annotation.Nullable
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator


class DemoJobCreator : JobCreator {
    @Nullable
    override fun create(tag: String): Job? {
        return when (tag) {
            MyDailyJob.TAG -> MyDailyJob()
            else -> null
        }
    }
}