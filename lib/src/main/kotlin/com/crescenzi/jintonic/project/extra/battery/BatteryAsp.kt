@file:Suppress("unused")

package com.crescenzi.jintonic.project.extra.battery

import android.content.Context
import com.crescenzi.jintonic.common.Values._ID
import com.crescenzi.jintonic.common.extension.getJintonicDecorator
import com.crescenzi.jintonic.data.DeviceRepo.application
import com.crescenzi.jintonic.domain.exceptions.JINTONIC_CODE
import com.crescenzi.jintonic.domain.exceptions.drunk
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before

@Aspect
class BatteryAsp {

    @Before("execution(@$_ID.project.extra.battery.MinBattery * *(..))")
    fun checkBattery(jp: JoinPoint) {
        val decorator = jp.getJintonicDecorator<MinBattery>()
        val bm = application.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
        val percentage = bm.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)

        if (decorator.minOrEqualValue !in 0..100) {
            drunk(
                message = "The battery percentage must be between 0 and 100 in the method ${jp.signature.name}",
                JINTONIC_CODE.SYSTEM
            )
        }
        if (percentage == Int.MIN_VALUE) {
            drunk(
                message = "Cannot execute ${jp.signature.name} because battery level is unavailable",
                JINTONIC_CODE.SYSTEM
            )
        }
        if (percentage < decorator.minOrEqualValue) {
            drunk(
                message = "Cannot Execute ${jp.signature.name} cause battery percentage is under ${decorator.minOrEqualValue}",
                JINTONIC_CODE.NOT_ENOUGH_BATTERY
            )
        }
    }

}