package de.bergerapps.icewarning.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

        restAPI.getEiswarnung(context, eiswarnungLiveData, lat, lng)
    }
}
