package com.crescenzi.jintonic.project.internet

import com.crescenzi.jintonic.common.Values.defaultValueForThrowParam

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireInternet(val mustThrow: Boolean = defaultValueForThrowParam)