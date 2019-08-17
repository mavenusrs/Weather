package com.mavenusrs.weather.permission

import io.reactivex.Single

interface PermissionHandler {

    fun checkHasPermission(): Boolean

    fun requestPermission(): Single<PermissionResult>

    fun onPermissionResult(granted: Boolean)

    enum class PermissionResult{
        GRANTED, DENIED_SOFT, DENIED_HARD
    }
}