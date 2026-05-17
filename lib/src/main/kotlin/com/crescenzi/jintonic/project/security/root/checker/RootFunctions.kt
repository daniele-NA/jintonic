package com.crescenzi.jintonic.project.security.root.checker

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import java.io.File

internal fun isRooted(context: Application): Boolean {
    val suPaths = listOf(
        "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
        "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
        "/system/bin/failsafe/su", "/data/local/su"
    )

    val rootApps = listOf(
        "com.noshufou.android.su", "com.thirdparty.superuser", "eu.chainfire.supersu",
        "com.koushikdutta.superuser", "com.zachspong.temprootremovejb", "com.ramdroid.appquarantine"
    )

    return Build.TAGS?.contains("test-keys") == true ||
            suPaths.any { File(it).exists() } ||
            try {
                val installed = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                    .map { it.packageName }
                rootApps.any { it in installed }
            } catch (_: Exception) { false } ||
            listOf("/system/xbin/which su", "/system/bin/which su", "which su").any {
                try { Runtime.getRuntime().exec(it).waitFor() == 0 } catch (_: Exception) { false }
            }
}