package de.bergerapps.icewarning.prediction

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobRequest
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.service.repository.RestAPI
import de.bergerapps.icewarning.util.SharedPrefUtil
import java.util.concurrent.TimeUnit


class PredictionDailyJob : DailyJob() {

    private val restAPI = RestAPI()

    override fun onRunDailyJob(params: Params): DailyJobResult {
        if (SharedPrefUtil().getLastLocation(context).isNotEmpty()) {
            val latLng = SharedPrefUtil().getLastLocation(context)
            restAPI.getEiswarnung(
                context,
                latLng.split(":")[0],
                latLng.split(":")[1]
            ) {
                if (it?.result == null)
                    return@getEiswarnung

                val builder = NotificationCompat.Builder(context, "88")
                    .setSmallIcon(R.drawable.ic_stat_ac_unit)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(it.result?.forecastText + " - " + it.result?.forecastCity)
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
        }

        return DailyJobResult.SUCCESS
    }

    companion object {

        const val TAG = "MyDailyJob"
        var startHour = TimeUnit.HOURS.toMillis(15)

        fun schedule(context: Context) {
            startHour = TimeUnit.HOURS.toMillis(
                context.getSharedPreferences("frostwarning", 0).getLong(
                    "start",
                    15
                )
            )

            schedule(
                JobRequest.Builder(TAG),
                startHour + 0,
                startHour + 1
            )
        }
    }
}