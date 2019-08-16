package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AstroEntity(
    @SerializedName("sunrise")
    @Expose
    val sunrise: String,
    @SerializedName("sunset")
    @Expose
    val sunset: String,
    @SerializedName("moonrise")
    @Expose
    val moonrise: String,
    @SerializedName("moonset")
    @Expose
    val moonset: String
)