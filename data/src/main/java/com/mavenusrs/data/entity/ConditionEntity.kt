package com.mavenusrs.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConditionEntity(
    @SerializedName("text")
    @Expose
    var text: String,
    @SerializedName("icon")
    @Expose
    val icon: String,
    @SerializedName("code")
    @Expose
    val code: Int
)