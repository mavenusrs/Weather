package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastEntity (
    @SerializedName("forecastday")
    @Expose
    val forecastdayEntity: List<ForecastdayEntity>)