@file:Suppress("unused")

package com.crescenzi.jintonic.project.security.screen

import android.app.Activity
import android.view.WindowManager
import com.crescenzi.jintonic.common.Values._ID
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class SecureWindowAsp {


    @Pointcut("within(@$_ID.project.security.screen.SecureWindow * )")
    fun decoratedActivity() {
    }

    @Pointcut("execution(* *.onCreate(android.os.Bundle))")
    fun onCreateMethod() {
    }

    @Around("decoratedActivity() && onCreateMethod()")
    @Throws(Throwable::class)
    fun secureWindow(pjp: ProceedingJoinPoint): Any? {
        val result = pjp.proceed()
        val activity = pjp.target as? Activity
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        return result
    }


}