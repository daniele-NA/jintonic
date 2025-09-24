package com.crescenzi.jintonic.project.profiling.time

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

/**
    NO PERMISSIONS REQUIRED

No special manifest permissions are required.

The method's execution is not modified; only the duration is logged.

 */

annotation class Timed