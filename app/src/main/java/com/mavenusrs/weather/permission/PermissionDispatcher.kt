package com.mavenusrs.weather.permission

class PermissionDispatcher(val permissionHandler: PermissionHandler){

    fun dispatchResult(granted: Boolean){
        permissionHandler.onPermissionResult(granted)
    }
}