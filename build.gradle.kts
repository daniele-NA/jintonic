@file:Suppress("AndroidGradlePluginVersion","NewerVersionAvailable")
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
    id("org.jetbrains.kotlin.jvm") version "2.2.10" apply false
    id("com.android.library") version "8.12.2" apply false
}

/**
 * GLOBAL VARIABLES
 */
gradle.rootProject {
    ext["min_version"] = 26 // BEFORE 26 AspectJ doesn't seem to work correctly
    ext["compile_version"] = 36
    ext["target_version"] = 36
    ext["jvm_version"] = "17"

    ext["package"] = "com.crescenzi.jintonic"
}