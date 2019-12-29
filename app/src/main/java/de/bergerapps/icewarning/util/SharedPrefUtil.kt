package de.bergerapps.icewarning.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil {
    val SHARED_PREF_FROST_WARNING = "SHARED_PREF_FROST_WARNING"

    fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_FROST_WARNING, 0)
    }

    fun getLastLocation(context: Context): String {
        return getSharedPref(context).getString("last_location", "")!!
    }

    fun getTimePrediction(context: Context): Long {
        return getSharedPref(context).getLong("time_prediction", 15)
    }

    fun setLastLocation(lat: String, long: String, context: Context) {
        getSharedPref(context).edit().putString("last_location", "$lat:$long").apply()
    }

    fun setTimePrediction(hour: Long, context: Context) {
        getSharedPref(context).edit().putLong("time_prediction", 15).apply()
    }
}