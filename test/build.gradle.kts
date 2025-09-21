@file:Suppress("UnstableApiUsage","UNCHECKED_CAST","UseTomlInstead")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.crescenzi.jintonic.gradle")  // TRANSITIVE
}

android {
    namespace = rootProject.extra["package"] as String
    compileSdk = rootProject.extra["compile_version"] as Int

    defaultConfig {
        applicationId = rootProject.extra["package"] as String
        minSdk = rootProject.extra["min_version"] as Int
        targetSdk = rootProject.extra["target_version"] as Int
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.material3:material3:1.3.2")

    implementation(project(":lib"))
}