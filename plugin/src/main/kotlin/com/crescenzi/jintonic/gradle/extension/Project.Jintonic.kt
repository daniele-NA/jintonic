@file:Suppress("UNCHECKED_CAST")

package com.crescenzi.jintonic.gradle.extension

import com.android.build.gradle.tasks.ExtractAnnotations
import com.crescenzi.jintonic.gradle.core.Values
import com.crescenzi.jintonic.gradle.core.Values.LOG_PREFIX
import com.hiya.plugins.JacocoAndroidUnitTestReportExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.kotlin.dsl.named
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.jintonicLog(msg: String) = logger.warn("$LOG_PREFIX $msg")
internal fun TaskContainer.namedOrNull(taskName: String) =
    try { named<Task>(taskName) } catch (_: UnknownDomainObjectException) { null }


internal fun Project.javaCompileTask(variantName: String): AbstractCompile? =
    firstTask(Values.JAVA_COMPILE_TASK_TEMPLATE.format(variantName)) as? AbstractCompile

internal fun Project.kotlinCompileTask(variantName: String): KotlinCompile? =
    firstTask(Values.KOTLIN_COMPILE_TASK_TEMPLATE.format(variantName)) as? KotlinCompile?

internal fun Project.kaptTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.KAPT_TASK_TEMPLATE.format(variantName))

internal fun Project.javaCompileTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.JAVA_COMPILE_TASK_TEMPLATE.format(variantName))

internal fun Project.kotlinCompileTaskProvider(variantName: String) =
    tasks.namedOrNull(Values.KOTLIN_COMPILE_TASK_TEMPLATE.format(variantName))

internal inline fun <reified T : Task> Project.taskProviderOrNull(taskName: String) =
    try { tasks.named<T>(taskName) } catch (_: UnknownDomainObjectException) { null }

internal fun Project.jacocoReportTaskProvider(variantName: String) =
    taskProviderOrNull<JacocoReport>(Values.JACOCO_REPORT_TASK_TEMPLATE.format(variantName))

internal fun Project.extractAnnotationsTaskProvider(variantName: String) =
    taskProviderOrNull<ExtractAnnotations>(Values.EXTRACT_ANNOTATIONS_TASK_TEMPLATE.format(variantName))

internal fun Project.jacocoAndroidReportExtension() =
    extensions.findByName(Values.JACOCO_ANDROID_REPORT_EXTENSION) as? JacocoAndroidUnitTestReportExtension

internal fun Project.firstTask(taskName: String) = getTasksByName(taskName, false).firstOrNull()