package com.mavenusrs.data.remote

import com.mavenusrs.data.entity.WeatherEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeaterApi {

    @GET("forecast.json")
    fun forcast(@Query("key") key: String, @Query("q") query: String,
                @Query("days") days: Int): Single<WeatherEntity>

}