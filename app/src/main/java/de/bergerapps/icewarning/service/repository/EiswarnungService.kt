package de.bergerapps.icewarning.service.repository

import de.bergerapps.icewarning.service.model.Eiswarnung
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EiswarnungService {
    /**
     * @GET declares an HTTP GET request
     */
    @GET("/")
    fun getForecast(@Query("key") key: String, @Query("lat") lat: String, @Query("lng") lng: String): Call<Eiswarnung>
}