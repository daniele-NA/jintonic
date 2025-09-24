@file:SuppressLint("PrivateApi", "MissingPermission")
@file:Suppress("unused", "FunctionName")

package com.crescenzi.jintonic.project.internet

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.crescenzi.jintonic.common.LOG_INFO
import com.crescenzi.jintonic.common.Values.defaultValueForThrowParam
import com.crescenzi.jintonic.common.Values.id
import com.crescenzi.jintonic.common.extension.getJintonicDecorator
import com.crescenzi.jintonic.data.DeviceRepo.application
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before


/**
 * REQUIRE MANIFEST PERMISSIONS :

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>

 */
@Aspect
class InternetAsp {

    var mustThrow: Boolean = defaultValueForThrowParam


    @Before("execution(@$id.project.internet.RequireInternet * *(..))")
    fun check_network(joinPoint: JoinPoint) {

        val decorator = joinPoint.getJintonicDecorator<RequireInternet>()
        mustThrow = decorator.mustThrow

        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network =
            connectivityManager.activeNetwork ?: serviceError("Cannot detect Internet Service")

        val capabilities = connectivityManager.getNetworkCapabilities(network as Network?)
        if (capabilities == null) serviceError("Cannot detect Internet Service ")

        if (!capabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) noInternet()
    }

    private fun noInternet() {
        if (mustThrow) drunk(message = "No Internet Available", code = JINTONIC_CODE.NO_INTERNET)
    }

    /**
     * IT DOESN'T CARE ABOUT USER PREFERENCES,THIS METHOD REFERS TO SYSTEM ERRORS LIKE PERMISSIONS
     */
    private fun serviceError(message: String) {
        LOG_INFO("Internet Service Error")
        drunk(message = message, code = JINTONIC_CODE.SYSTEM)
    }
}