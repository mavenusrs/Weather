package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DayEntity (
    @SerializedName("maxtemp_c")
    @Expose
    val maxtempC: Double,
    @SerializedName("maxtemp_f")
    @Expose
    val maxtempF: Double,
    @SerializedName("mintemp_c")
    @Expose
    val mintempC: Double,
    @SerializedName("mintemp_f")
    @Expose
    val mintempF: Double,
    @SerializedName("avgtemp_c")
    @Expose
    val avgtempC: Double,
    @SerializedName("avgtemp_f")
    @Expose
    val avgtempF: Double,
    @SerializedName("maxwind_mph")
    @Expose
    val maxwindMph: Double,
    @SerializedName("maxwind_kph")
    @Expose
    val maxwindKph: Double,
    @SerializedName("totalprecip_mm")
    @Expose
    val totalprecipMm: Double,
    @SerializedName("totalprecip_in")
    @Expose
    val totalprecipIn: Double,
    @SerializedName("avgvis_km")
    @Expose
    val avgvisKm: Double,
    @SerializedName("avgvis_miles")
    @Expose
    val avgvisMiles: Double,
    @SerializedName("avghumidity")
    @Expose
    val avghumidity: Double,
    @SerializedName("condition")
    @Expose
    val conditionEntity: ConditionEntity,
    @SerializedName("uv")
    @Expose
    val uv: Double)