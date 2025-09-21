@file:Suppress("unused","FunctionName")

package com.crescenzi.jintonic

import com.crescenzi.jintonic.logger.LOG
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before


@Aspect
class AspectTest {


    /* CLASSI:
    @Before("within(@com.crescenzi.jintonic.DebugLog *)")


     */

    @Before("execution(@com.crescenzi.jintonic.DebugLog * *(..))")
    fun debug_log(joinPoint: JoinPoint){
        LOG("Debug Log Detected")
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