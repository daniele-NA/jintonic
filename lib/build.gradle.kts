@file:Suppress("UnstableApiUsage","UNCHECKED_CAST")
//noinspection UseTomlInstead
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    id("com.ibotta.gradle.aop") version "1.4.1"
}

android {
    namespace = rootProject.extra["package"] as String
    compileSdk = rootProject.extra["compile_version"] as Int

    defaultConfig {
        minSdk = rootProject.extra["min_version"] as Int
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
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
        freeCompilerArgs += rootProject.extra["compiler_args"] as List<String>
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")


    api("org.aspectj:aspectjrt:1.9.24")
}
