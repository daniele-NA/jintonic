@file:Suppress("unused", "FunctionName")

package com.crescenzi.jintonic.common

import android.util.Log
import com.crescenzi.jintonic.common.Values.tag
import com.crescenzi.jintonic.BuildConfig

/**
 * We use Log.e also if there are no errors
 */
internal fun LOG_INFO(value: String?) {
    if (BuildConfig.DEBUG) {
        Log.wtf(tag, value.toString())
    }
}