package com.crescenzi.jintonic.data

import android.annotation.SuppressLint
import android.app.Application
import com.crescenzi.jintonic.common.LOG_INFO
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk

/**
 * CURRENT DEVICE APPLICATION & PARAMS
 **/
@SuppressLint("PrivateApi")
object DeviceRepo {

    var application: Application

    /*
    It Stops the runtime if the application is NULL
    */
    init {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        application = activityThreadClass.getMethod("getApplication").invoke(currentActivityThread) as? Application ?: drunk("CANNOT DETECT APPLICATION",
            JINTONIC_CODE.SYSTEM
        )
        LOG_INFO("App Detected")
    }

}