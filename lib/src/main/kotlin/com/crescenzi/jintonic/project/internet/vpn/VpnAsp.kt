@file:Suppress("unused", "MissingPermission")

package com.crescenzi.jintonic.project.internet.vpn

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_VPN
import com.crescenzi.jintonic.common.Values.id
import com.crescenzi.jintonic.data.DeviceRepo.application
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class VpnAsp {


    @Before("execution(@$id.project.internet.vpn.RequireVpn * *(..))")
    fun checkVpn(jp: JoinPoint) {
        if (!isVpnActive()) {
            drunk("Cannot Execute ${jp.signature.name} cause VPN is NOT active", JINTONIC_CODE.VPN_REQUIRED)
        }

    }

    private fun isVpnActive(): Boolean {
        val service = application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = service.allNetworks
        networks.forEach {
            val caps = service.getNetworkCapabilities(it)
            if (caps?.hasTransport(TRANSPORT_VPN) == true) {
                return true
            }
        }
        return false
    }

}