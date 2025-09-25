@file:SuppressLint("PrivateApi", "MissingPermission")
@file:Suppress("unused", "FunctionName")

package com.crescenzi.jintonic.project.internet.status

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.crescenzi.jintonic.common.Values.defaultValueForThrowParam
import com.crescenzi.jintonic.common.Values.id
import com.crescenzi.jintonic.common.extension.getJintonicDecorator
import com.crescenzi.jintonic.data.DeviceRepo.application
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect


@Aspect
class InternetAsp {

    var mustThrow: Boolean = defaultValueForThrowParam


    @Around("execution(@$id.project.internet.status.RequireInternet * *(..))")
    fun checkNetwork(pjp: ProceedingJoinPoint): Any? {

        val decorator = pjp.getJintonicDecorator<RequireInternet>()
        mustThrow = decorator.mustThrow

        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network =
            connectivityManager.activeNetwork

        if (network == null) return noInternet(pjp.signature.name)

        val capabilities = connectivityManager.getNetworkCapabilities(network as Network?)
        if (capabilities == null) return noInternet(pjp.signature.name)

        if (!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        )return noInternet(pjp.signature.name)

        return pjp.proceed()
    }

    private fun noInternet(methodName: String): Nothing? {
        if (mustThrow) drunk(message = "No Internet Available for $methodName ", code = JINTONIC_CODE.NO_INTERNET)
        return null
    }
}