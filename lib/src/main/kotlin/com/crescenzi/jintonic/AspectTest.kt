@file:Suppress("unused", "FunctionName")

package com.crescenzi.jintonic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.crescenzi.jintonic.logger.LOG
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*

/**
 *
 *
 * QUANDO CAMBI NOMI,VEDI I PROGUARD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */
@Aspect
class AspectTest {


    /*

    JinTonic.checkNetwork().requireBattery(20).execute { apiCall() }

     */


    private val application: Application?

    init {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val currentActivityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        application = activityThreadClass.getMethod("getApplication").invoke(currentActivityThread) as? Application
    }


    /* CLASSI:
    @Before("within(@com.crescenzi.jintonic.DebugLog *)")


     */

    @Around("execution(@com.crescenzi.jintonic.DebugLog * *(..))")
    fun debug_log(proceedingJoinPoint: ProceedingJoinPoint) {
        LOG("LOG Before proceed()")
        proceedingJoinPoint.proceed()
    }

    @SuppressLint("PrivateApi")
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Before("execution(@com.crescenzi.jintonic.RequireNetwork * *(..))")
    fun check_network(joinPoint: JoinPoint) {
        val connectivityManager =
            application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network =
            connectivityManager.activeNetwork ?: throw IllegalStateException("No internet")
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: throw IllegalStateException("No internet")
        if (!capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) throw IllegalStateException("No internet")
    }


//    @Before("execution(* me.crescenzi.*.*.onCreate(android.os.Bundle))")
//    fun onCreateAdvice(joinPoint: JoinPoint?) {
//        if (joinPoint?.getThis() != null) {
//            LOG("Activity Event")
//        }
//    }
//
//
//    @Pointcut("execution(void *.onClick(..))")
//    fun onButtonClick() {
//    }
//
//    @Before("onButtonClick() && args(view)")
//    fun onClickAdvice(view: View?) {
//        if (view is TextView) {
//            // Logger.logItem("${view.text} clicked")
//        }
//    }
}