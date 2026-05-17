package com.crescenzi.jintonic.project.security.root

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

/**
REQUIRE MANIFEST QUERIES:

<!--
Only checks whether certain known root apps are installed. Ignores the authorization (which scans all packages), which may be rejected by the store.
-->

<queries>
    <package android:name="com.noshufou.android.su" />
    <package android:name="com.thirdparty.superuser" />
    <package android:name="eu.chainfire.supersu" />
    <package android:name="com.koushikdutta.superuser" />
    <package android:name="com.zachspong.temprootremovejb" />
    <package android:name="com.ramdroid.appquarantine" />
</queries>


Checks if the device is rooted before executing the annotated method.

@param forRoot If true, the method is executed only on root-enabled devices;
if false, the method is executed only on non-rooted devices.
 */
annotation class WithRoot(val forRoot: Boolean)

