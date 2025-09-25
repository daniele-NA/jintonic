package com.crescenzi.jintonic.project.extra.battery

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)

/**
 * NO PERMISSION REQUIRED
 *
 * Ensures that the current battery level is greater than or equal to [minOrEqualValue].
 * If the battery level is below [minOrEqualValue], an exception will be thrown.
 * Range 0-100
 */
annotation class MinBattery(val minOrEqualValue: Int)
