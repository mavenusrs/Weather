package com.mavenusrs.weather.permission


class PermissionDispatcher (private val permissionHandler: PermissionHandler){

    fun dispatchResult(granted: Boolean){
        permissionHandler.onPermissionResult(granted)
    }
}