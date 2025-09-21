@file:Suppress("unused","FunctionName")

package com.crescenzi.jintonic

import com.crescenzi.jintonic.logger.LOG
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*


@Aspect
class AspectTest {


    /* CLASSI:
    @Before("within(@com.crescenzi.jintonic.DebugLog *)")


     */

    @Around("execution(@com.crescenzi.jintonic.DebugLog * *(..))")
    fun debug_log(proceedingJoinPoint: ProceedingJoinPoint){
        LOG("LOG Before proceed()")
        proceedingJoinPoint.proceed()
    }


//    @Before("execution(* me.crescenzi.*.*.onCreate(android.os.Bundle))")
//    fun onCreateAdvice(joinPoint: JoinPoint?) {
//        if (joinPoint?.getThis() != null) {
//            LOG("Activity Event")
//        }
//    }
//
//
//    @Pointcut("execution(void *.onClick(..))")
//    fun onButtonClick() {
//    }
//
//    @Before("onButtonClick() && args(view)")
//    fun onClickAdvice(view: View?) {
//        if (view is TextView) {
//            // Logger.logItem("${view.text} clicked")
//        }
//    }
}