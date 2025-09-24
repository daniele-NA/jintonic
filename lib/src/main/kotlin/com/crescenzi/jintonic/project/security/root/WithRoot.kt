package com.crescenzi.jintonic.project.security.root

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

/**
REQUIRE MANIFEST PERMISSIONS:

<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>


Checks if the device is rooted before executing the annotated method.

@param forRoot If true, the method is executed only on root-enabled devices;
if false, the method is executed only on non-rooted devices.
 */
annotation class WithRoot(val forRoot: Boolean)

