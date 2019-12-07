package de.bergerapps.icewarning.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.service.model.Eiswarnung
import de.bergerapps.icewarning.service.repository.RestAPI


class MainViewModel : ViewModel() {
    var lastLat = ""
    var lastLng = ""

    val eiswarnungLiveData = MutableLiveData<Eiswarnung>()

    private val restAPI = RestAPI()

    fun getEiswarnung(context: Context, lat: String, lng: String) {
        if (lastLat == lat && lastLng == lng) {
            return
        }
        lastLat = lat
        lastLng = lng

        restAPI.getEiswarnung(context, lat, lng) {
            eiswarnungLiveData.value = it
        }
    }
}
