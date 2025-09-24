package com.crescenzi.jintonic.project.internet.status

import com.crescenzi.jintonic.common.Values.defaultValueForThrowParam


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)


/**
REQUIRE MANIFEST PERMISSIONS:

<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>

Checks if there is an active Internet connection before executing the annotated method.


@param mustThrow If 'mustThrow' is true, it throws an exception when there is no Internet,
otherwise it SKIPS the method execution. [ DEFAULT VALUE = TRUE ]
 */

annotation class RequireInternet(val mustThrow: Boolean = defaultValueForThrowParam)