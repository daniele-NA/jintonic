@file:Suppress("UnstableApiUsage", "UNCHECKED_CAST", "UseTomlInstead")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.crescenzi.jintonic.gradle")  // TRANSITIVE
}

android {
    namespace = rootProject.extra["package"] as String
    compileSdk = rootProject.extra["compile_version"] as Int

    defaultConfig {
        minSdk = rootProject.extra["min_version"] as Int
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = rootProject.extra["jvm_version"] as String
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")


    api("org.aspectj:aspectjrt:1.9.24")  //TRANSITIVE
}
