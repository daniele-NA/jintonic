@file:Suppress("AndroidGradlePluginVersion","NewerVersionAvailable")
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("com.gradle.plugin-publish") version "1.2.1" //GRADLE PORTAL
}

/**
  ==== IT DOESN'T DEPEND ON PARENT GRADLE FILES ====
**/
repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("com.hiya:jacoco-android:0.2")
    implementation("org.aspectj:aspectjtools:1.9.7")
    implementation("org.aspectj:aspectjrt:1.9.7")
}

gradlePlugin {
    plugins {
        create("JinPlugin") {
            id ="io.github.daniele-NA.gradle"
            implementationClass ="com.crescenzi.jintonic.gradle.JinPlugin"

            //GRADLE PORTAL

            // Keys under .username/.gradle/gradle.properties

            // Published with   ./gradlew :plugin:publishPlugins

            displayName = "Jintonic Gradle Plugin"
            description = "AOP Weaving gradle plugin"
            group = "io.github.daniele-NA"
            version = "1.0.0"
            website ="https://github.com/daniele-NA/jintonic"
            vcsUrl = "https://github.com/daniele-NA/jintonic"
            tags= listOf("aop", "aspectj", "weaving", "android")
        }
    }
}