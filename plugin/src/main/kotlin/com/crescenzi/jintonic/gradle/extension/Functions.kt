@file:Suppress("UNCHECKED_CAST")

package com.crescenzi.jintonic.gradle.extension

import com.android.build.gradle.tasks.ExtractAnnotations
import com.crescenzi.jintonic.gradle.core.Values
import com.crescenzi.jintonic.gradle.core.Values.LOG_PREFIX
import com.hiya.plugins.JacocoAndroidUnitTestReportExtension
import org.gradle.api.*
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File


fun Project.aopLog(msg: String) = logger.warn("$LOG_PREFIX $msg")

fun Project.javaCompileTask(variantName: String): AbstractCompile? =
    firstTask(Values.JAVA_COMPILE_TASK_TEMPLATE.format(variantName)) as? AbstractCompile

fun Project.kotlinCompileTask(variantName: String): KotlinCompile? =
    firstTask(Values.KOTLIN_COMPILE_TASK_TEMPLATE.format(variantName)) as? KotlinCompile?

fun Project.kaptTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.KAPT_TASK_TEMPLATE.format(variantName))

fun Project.javaCompileTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.JAVA_COMPILE_TASK_TEMPLATE.format(variantName))

fun Project.kotlinCompileTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.KOTLIN_COMPILE_TASK_TEMPLATE.format(variantName))

fun Project.jacocoReportTaskProvider(variantName: String): TaskProvider<JacocoReport>? {
    val jacocoTask = tasks.namedOrNull(Values.JACOCO_REPORT_TASK_TEMPLATE.format(variantName))

    return if (jacocoTask != null) {
        jacocoTask as TaskProvider<JacocoReport>
    } else {
        null
    }
}

fun Project.extractAnnotationsTaskProvider(variantName: String): TaskProvider<ExtractAnnotations>? {
    val extractAnnotationsTask =
        tasks.namedOrNull(Values.EXTRACT_ANNOTATIONS_TASK_TEMPLATE.format(variantName))

    return if (extractAnnotationsTask != null) {
        extractAnnotationsTask as TaskProvider<ExtractAnnotations>
    } else {
        null
    }
}

fun Project.jacocoAndroidReportExtension(): JacocoAndroidUnitTestReportExtension? {
    val jacocoExt = extensions.findByName(Values.JACOCO_ANDROID_REPORT_EXTENSION)

    return if (jacocoExt != null) {
        jacocoExt as JacocoAndroidUnitTestReportExtension
    } else {
        null
    }
}

fun Project.firstTask(taskName: String) = getTasksByName(taskName, false).firstOrNull()

fun TaskContainer.namedOrNull(taskName: String): TaskProvider<Task>? {
    return try {
        named(taskName)
    } catch (_: UnknownDomainObjectException) {
        null
    }
}

fun File?.isEmpty() = ((this == null) || (list() == null) || list().isEmpty())
