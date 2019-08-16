package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationEntity (
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("country")
    @Expose
    val country: String,
    @SerializedName("lat")
    @Expose
    val lat: Double,
    @SerializedName("lon")
    @Expose
    val lon: Double,
    @SerializedName("tz_id")
    @Expose
    val tzId: String,
    @SerializedName("localtime_epoch")
    @Expose
    val localtimeEpoch: Int,
    @SerializedName("localtime")
    @Expose
    val localtime: String)