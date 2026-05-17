@file:Suppress("UnstableApiUsage", "UNCHECKED_CAST", "UseTomlInstead")


/**
 * The dependency `api("org.aspectj:aspectjrt:1.9.24")` works only for local builds.
 *
 * Since this is an Android library module (not a JVM module), `aspectjrt` is not automatically
 * included in the consumer's classpath. To ensure the AAR works correctly for downstream users,
 * we explicitly add it to the POM:
 *
 * pom {
 *     withXml {
 *         asNode().appendNode("dependencies").apply {
 *             appendNode("dependency").apply {
 *                 appendNode("groupId", "org.aspectj")
 *                 appendNode("artifactId", "aspectjrt")
 *                 appendNode("version", "1.9.24")
 *                 appendNode("scope", "compile")
 *             }
 *         }
 *     }
 * }
 *
 * NOTE: If building the AAR locally, it will NOT work for consumers unless they also add:
 * implementation("org.aspectj:aspectjrt:1.9.24")
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    id("io.github.daniele-NA.gradle")
    id("maven-publish") // JITPACK
}

// JITPACK //
afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                groupId = "com.github.daniele-NA"
                artifactId = "jintonic"
                version = "1.0.1"
                artifact("$buildDir/outputs/aar/lib-release.aar")

                // ==== For aspectJ classpath ==== //
                pom {
                    withXml {
                        asNode().appendNode("dependencies").apply {
                            appendNode("dependency").apply {
                                appendNode("groupId", "org.aspectj")
                                appendNode("artifactId", "aspectjrt")
                                appendNode("version", "1.9.24")
                                appendNode("scope", "compile")
                            }
                        }
                    }
                }
            }
        }
    }
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
