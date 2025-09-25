package com.crescenzi.jintonic.project.internet.vpn

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

/**
Permissions required :

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>


If a VPN is active in the annotated method, an exception will be thrown.

 */
annotation class RequireVpn
