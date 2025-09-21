package com.crescenzi.jintonic.gradle.core

/**
 * APP CONSTANTS
 */
internal object Values {

    // === GLOBAL === //
    internal const val LOG_PREFIX = "\uD83C\uDF79 JINTONIC GRADLE PLUGIN =>"
    internal const val ADDITIONAL_COMPILER_FLAG_1 = "-Xsam-conversions=class"


    // === PLUGIN === //
    internal const val MISSING_PLUGIN_ERROR = "'com.android.application' or 'com.android.library' plugin required."
    internal const val ANDROID_EXTENSION_NAME = "android"
    internal const val ANDROID_JAR_TEMPLATE = "%s/platforms/%s/android.jar"
    internal const val PRE_WEAVE_DIR_TEMPLATE = "sober/%s/%s"
    internal const val POST_WEAVE_DIR_TEMPLATE = "drunk/%s"
    internal const val PATTERN_ORIGINAL_KOTLINC_OUTPUT_DIR = "tmp/kotlin-classes/"
    internal const val AOP_WEAVE_TASK = "aopWeave%s"
    internal const val AOP_LOG = "jintonic.log"
    internal const val LANG_JAVA = "Java"
    internal const val LANG_KOTLIN = "Kotlin"


    // === FUNCTIONS === //
    internal const val KAPT_TASK_TEMPLATE = "kapt%sKotlin"
    internal const val JAVA_COMPILE_TASK_TEMPLATE = "compile%sJavaWithJavac"
    internal const val KOTLIN_COMPILE_TASK_TEMPLATE = "compile%sKotlin"
    internal const val JACOCO_REPORT_TASK_TEMPLATE = "jacocoTest%sUnitTestReport"
    internal const val EXTRACT_ANNOTATIONS_TASK_TEMPLATE = "extract%sAnnotations"
    internal const val JACOCO_ANDROID_REPORT_EXTENSION = "jacocoAndroidUnitTestReport"


    // === EXECUTOR === //
    internal const val CMD_IN_PATH = "-inpath"
    internal const val CMD_ASPECT_PATH = "-aspectpath"
    internal const val CMD_OUTPUT_DIR = "-d"
    internal const val CMD_CLASS_PATH = "-classpath"
    internal const val CMD_BOOT_CLASS_PATH = "-bootclasspath"
    internal const val CMD_LOG = "-log"
    internal const val CMD_SHOW_WEAVE_INFO = "-showWeaveInfo"
    internal const val CMD_JAVA_1_8 = "-1.8"
}