package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastdayEntity (
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("date_epoch")
    @Expose
    val dateEpoch: Int,
    @SerializedName("dayEntity")
    @Expose
    val dayEntity: DayEntity,
    @SerializedName("astroEntity")
    @Expose
    val astroEntity: AstroEntity

    )