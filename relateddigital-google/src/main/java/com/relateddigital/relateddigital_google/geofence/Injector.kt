package com.relateddigital.relateddigital_google.geofence

enum class Injector {
    INSTANCE;

    var gpsManager: GpsManager? = null
        private set

    fun initGpsManager(gpsManager: GpsManager?) {
        this.gpsManager = gpsManager
    }
}