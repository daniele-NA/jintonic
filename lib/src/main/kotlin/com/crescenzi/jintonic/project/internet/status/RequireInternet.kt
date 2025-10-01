package com.crescenzi.jintonic.project.internet.status


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)


/**
REQUIRE MANIFEST PERMISSIONS:

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>

Checks if there is an active Internet connection before executing the annotated method.
Otherwise throws Exception
 */

annotation class RequireInternet