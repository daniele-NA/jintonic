@file:SuppressLint("PrivateApi", "MissingPermission")
@file:Suppress("unused", "FunctionName")

package com.crescenzi.jintonic.project.profiling.time

import android.annotation.SuppressLint
import com.crescenzi.jintonic.common.LOG_INFO
import com.crescenzi.jintonic.common.Values.id
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect


@Aspect
class TimedAsp {

    @Around("execution(@$id.project.profiling.time.Timed * *(..))")
    fun measureTime(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        val result = proceedingJoinPoint.proceed()
        val end = System.currentTimeMillis()
        val durationSeconds = (end - start) / 1000.0
        LOG_INFO("Time occurred for ${proceedingJoinPoint.signature?.name} : $durationSeconds s")
        return result
    }

}