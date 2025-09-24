package com.crescenzi.jintonic.common.extension

import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.JintonicException
import com.crescenzi.jintonic.domain.exceptions.drunk
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature
import kotlin.jvm.Throws

@Throws(JintonicException::class)
internal inline fun <reified T : Annotation> JoinPoint.getJintonicDecorator(): T {
    val method = (signature as MethodSignature).method
    return method.getAnnotation(T::class.java)
        ?: drunk(message = "Annotation ${T::class.java.simpleName} not found on ${method.name}", code = JINTONIC_CODE.GENERIC)
}