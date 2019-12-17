package de.bergerapps.icewarning.service.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import de.bergerapps.icewarning.service.model.Eiswarnung
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RestAPI {

    private val data = MutableLiveData<Eiswarnung>()
    private val eiswarnungService: EiswarnungService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.eiswarnung.de/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        eiswarnungService = retrofit.create(EiswarnungService::class.java)
    }

    fun getEiswarnung(
        context: Context,
        lat: String,
        lng: String,
        callback: (Eiswarnung?) -> Unit
    ) {
        val properties = Properties()
        properties.load(context.assets.open("auth.properties"))
        val token = properties.getProperty("token")

        doAsync {
            eiswarnungService.getForecast(token, lat, lng).enqueue(object : Callback<Eiswarnung> {

                override fun onResponse(call: Call<Eiswarnung>?, response: Response<Eiswarnung>?) {
                    if (response?.body() != null) {
                        data.value = response.body()!!
                        callback(data.value!!)
                    } else {
                        data.value = null
                        callback(data.value!!)
                    }
                }

                override fun onFailure(call: Call<Eiswarnung>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                }
            })

        }
    }

}