package de.bergerapps.icewarning.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.service.repository.RestAPI

class ForecastWorker(
    private val context: Context, params: WorkerParameters) : Worker(context, params) {

    private val restAPI = RestAPI()

    override fun doWork(): Result {

        restAPI.getEiswarnung(context, "52.386822", "10.586550") {

            val builder = NotificationCompat.Builder(context, "88")
                .setSmallIcon(R.drawable.ic_stat_ac_unit)
                .setContentTitle(context.getString(de.bergerapps.icewarning.R.string.app_name))
                .setContentText(it?.result?.forecastText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)


            val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name =
                    context.getString(R.string.app_name)// The user-visible name of the channel.
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel("88", name, importance)
                mNotificationManager!!.createNotificationChannel(mChannel)
            }

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(88, builder.build())
            }
        }

        WorkerService.setupWorker(applicationContext)

        return Result.success()
    }
}