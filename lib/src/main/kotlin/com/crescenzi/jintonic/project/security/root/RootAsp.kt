@file:Suppress("unused")
package com.crescenzi.jintonic.project.security.root

import com.crescenzi.jintonic.common.LOG_INFO
import com.crescenzi.jintonic.common.Values.id
import com.crescenzi.jintonic.common.extension.getJintonicDecorator
import com.crescenzi.jintonic.data.DeviceRepo
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk
import com.crescenzi.jintonic.project.security.root.checker.isRooted
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before


@Aspect
class RootAsp {

    // We check it one time
    val isRooted = isRooted(DeviceRepo.application)
    init {
        LOG_INFO("DEVICE ROOTED : $isRooted")
    }

    @Before("execution(@$id.project.security.root.WithRoot * *(..))")
    fun checkRoot(joinPoint: JoinPoint) {

        val decorator = joinPoint.getJintonicDecorator<WithRoot>()

        if(!isRooted && decorator.forRoot){
            drunk("Cannot Execute ${joinPoint.signature.name} cause device is NOT rooted", code = JINTONIC_CODE.NON_ROOTED_DEVICE)
        }
        if(isRooted && !decorator.forRoot){
            drunk("Cannot Execute ${joinPoint.signature.name} cause device is rooted", code = JINTONIC_CODE.ROOTED_DEVICE)
        }

    }

}