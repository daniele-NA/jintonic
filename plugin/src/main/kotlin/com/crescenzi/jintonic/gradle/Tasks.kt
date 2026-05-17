package com.crescenzi.jintonic.gradle

import com.android.build.gradle.tasks.ExtractAnnotations
import com.crescenzi.jintonic.gradle.core.Values
import com.crescenzi.jintonic.gradle.core.swapContent
import com.crescenzi.jintonic.gradle.extension.jacocoAndroidReportExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Restores pre-weave classes from earlier compile tasks into the destination
 * folders. Executes Kapt. Then moves the classes back from the destination
 * folders into the pre-weave locations.
 *
 * Guarantees correct behavior for incremental builds.
 */

internal fun configureKaptTask(
    kaptTaskProvider: TaskProvider<Task>,
    kotlinCompileTaskProvider: TaskProvider<Task>?,
    javaCompileTaskProvider: TaskProvider<Task>?,
    preWeaveKotlinDir: Provider<Directory>,
    preWeaveJavaDir: Provider<Directory>
) {
    kaptTaskProvider.configure {
        swapContent(kotlinCompileTaskProvider?.get(), preWeaveKotlinDir)
        swapContent(javaCompileTaskProvider?.get(), preWeaveJavaDir)
    }
}

/**
 * Set the Kotlin compiler to emit classes to the pre-weave directory. Additional actions
 * will handle moving compiled classes. This preserves incremental build support and
 * prepares the project for AOP weaving.
 */

internal fun configureKotlinCompileTask(
    kotlinCompileProvider: TaskProvider<Task>,
    preWeaveKotlinDir: Provider<Directory>
) {
    kotlinCompileProvider.configure {
        outputs.dir(preWeaveKotlinDir)

        swapContent(this, preWeaveKotlinDir)
    }
}

/**
 * Set the Java compiler to write classes to the pre-weave directory. Additional actions
 * will manage moving compiled classes. This keeps incremental builds intact and
 * sets up for AOP weaving.
 */
internal fun configureJavaCompileTask(
    project: Project,
    javaCompileProvider: TaskProvider<Task>,
    preWeaveKotlinDir: Provider<Directory>,
    preWeaveJavaDir: Provider<Directory>
) {
    javaCompileProvider.configure {
        (this as AbstractCompile).classpath += project.files(preWeaveKotlinDir)
        outputs.dir(preWeaveJavaDir)

        swapContent(this, preWeaveJavaDir)
    }
}

/**
 * Update the JacocoReport task's class directories to include our post-weave folder.
 * The Jacoco plugin, [com.hiya.plugins.JacocoAndroidUnitTestReportExtension], uses these
 * directories to build its report. Omitting the post-weave folder results in an empty report.
 */
internal fun configureJacocoReportTask(
    jacocoReportTaskProvider: TaskProvider<JacocoReport>,
    postWeaveDir: Provider<Directory>
) {
    jacocoReportTaskProvider.configure {
        val jacocoExt = project.jacocoAndroidReportExtension()

        if (jacocoExt != null) {
            // This file tree represents the post-weave directory we've
            // defined, along with the excludes we set for the Jacoco project
            val postWeaveFiltered = project.fileTree(postWeaveDir).apply {
                setExcludes(jacocoExt.excludes)
            }
            // Re-set the Jacoco task's class directories to include our post-weave directory with our desired
            // excludes
            classDirectories.setFrom(classDirectories + postWeaveFiltered)
        }
    }
}

/**
 * At some point in the 7.0.X versions of the Android Gradle Plugin changes, an
 * "extract<Variant>Annotations" started appearing to run just after Kotlin compilation. This
 * task started to fail, as it no longer could find the
 * "<project>/build/tmp/kotlin-classes/<variant>" directory.
 *
 * Since this directory was never created due to how we are managing the output directories on
 * compilation tasks (in order to allow AOP weaving to occur), we need to make sure two things
 * happen:
 *
 * 1. Make sure that "extract<Variant>Annotations" task doesn't fail due to a missing directory.
 * We'll simply make sure this directory exists, as a workaround.
 *
 * 2. We need to ensure the actual Kotlin compilation output directory is included in the list
 * needed by this task. Unfortunately, "classpath" is what we'd like to modify, but it's
 * immutable. However, looking at the logic in the "ExtractAnnotations" class, it copies
 * everything from "bootClasspath" to "classpath" before executing. And "bootClasspath" is
 * mutable. So if we add the true compilation output directory to the "bootClasspath", it will
 * pick up the compiled classes correctly, and "just work".
 */
internal fun configureExtractAnnotationsTask(
    extractAnnotationsProvider: TaskProvider<ExtractAnnotations>,
    preWeaveKotlinDir: Provider<Directory>
) {
    extractAnnotationsProvider.configure {
        doFirst {
            classpath.files.forEach {
                if (it.path.contains(Values.PATTERN_ORIGINAL_KOTLINC_OUTPUT_DIR) && !it.exists()) {
                    it.mkdirs()
                }
            }
        }

        bootClasspath += project.files(preWeaveKotlinDir)
    }
}