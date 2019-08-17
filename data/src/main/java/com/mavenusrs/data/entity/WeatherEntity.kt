package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherEntity (
    @SerializedName("location")
    @Expose
    val location: LocationEntity,
    @SerializedName("current")
    @Expose
    val currentEntity: CurrentEntity,
    @SerializedName("forecast")
    @Expose
    val forecastEntity: ForecastEntity)