package com.crescenzi.jintonic.gradle.core

import com.crescenzi.jintonic.gradle.AopWeaveExtension
import com.crescenzi.jintonic.gradle.extension.jintonicLog
import com.crescenzi.jintonic.gradle.extension.isDirectoryEmpty
import com.crescenzi.jintonic.gradle.extension.javaCompileTask
import com.crescenzi.jintonic.gradle.extension.kotlinCompileTask
import com.crescenzi.jintonic.gradle.tasks.MainTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun gatherAspectPaths(
    project: Project,
    variantNameCapitalized: String,
    preWeaveJavaDir: Provider<Directory>,
    preWeaveKotlinDir: Provider<Directory>
): ConfigurableFileCollection {
    val javaCompileTask = project.javaCompileTask(variantNameCapitalized)
    val kotlinCompileTask = project.kotlinCompileTask(variantNameCapitalized)

    return when {
        ((javaCompileTask != null) && (kotlinCompileTask != null)) -> project.files(
            javaCompileTask.classpath,
            kotlinCompileTask.libraries,
            preWeaveJavaDir.get().asFile,
            preWeaveKotlinDir.get().asFile
        )

        (javaCompileTask != null) -> project.files(
            javaCompileTask.classpath,
            preWeaveJavaDir.get().asFile
        )

        (kotlinCompileTask != null) -> project.files(
            kotlinCompileTask.libraries,
            preWeaveKotlinDir.get().asFile
        )

        else -> throw RuntimeException("Either Java, Kotlin, or both types of compile task must be present.")
    }
}

internal fun gatherClassPaths(
    project: Project,
    variantNameCapitalized: String
): ConfigurableFileCollection {
    val javaCompileTask = project.javaCompileTask(variantNameCapitalized)
    val kotlinCompileTask = project.kotlinCompileTask(variantNameCapitalized)

    return when {
        ((javaCompileTask != null) && (kotlinCompileTask != null)) -> project.files(
            javaCompileTask.classpath,
            kotlinCompileTask.libraries
        )

        (javaCompileTask != null) -> project.files(javaCompileTask.classpath)
        (kotlinCompileTask != null) -> project.files(kotlinCompileTask.libraries)
        else -> throw RuntimeException("Either Java, Kotlin, or both types of compile task must be present.")
    }
}

internal fun Task.swapContent(
    task: Task?,
    preWeaveDir: Provider<Directory>
) {

    task?.let {
        if (task is AbstractCompile) {
            doFirst {
                if (!preWeaveDir.get().asFile.isDirectoryEmpty()) {
                    project.copy {
                        from(preWeaveDir)
                        into(task.destinationDirectory)
                    }
                }
            }

            doLast {
                project.copy {
                    from(task.destinationDirectory)
                    into(preWeaveDir)
                }

                project.delete(task.destinationDirectory)
            }

        }
        if (task is KotlinCompile) {
            doFirst {
                if (!preWeaveDir.get().asFile.isDirectoryEmpty()) {
                    project.copy {
                        from(preWeaveDir)
                        into(task.destinationDirectory)
                    }
                }
            }

            doLast {
                project.copy {
                    from(task.destinationDirectory)
                    into(preWeaveDir)
                }

                project.delete(task.destinationDirectory)
            }

        }
    }
}


internal fun configureAopWeaveTask(
    project: Project,
    extension: AopWeaveExtension,
    variantNameCapitalized: String,
    javaCompileProvider: TaskProvider<Task>?,
    kotlinCompileProvider: TaskProvider<Task>?,
    aopWeaveProvider: TaskProvider<MainTask>,
    androidJarPath: String,
    preWeaveJavaDir: Provider<Directory>,
    preWeaveKotlinDir: Provider<Directory>,
    postWeaveDir: Provider<Directory>
) {
    aopWeaveProvider.configure {
        targetPath = project.files(preWeaveJavaDir.get().asFile, preWeaveKotlinDir.get().asFile)
        aspectPathResolver = {
            project.files(gatherAspectPaths(project, variantNameCapitalized, preWeaveJavaDir, preWeaveKotlinDir))
                .also {
                    if (extension.filter.isNotEmpty()) {
                        project.jintonicLog("Weaving filter in ${project.name} is: ${extension.filter}")
                    }
                }
                .filter { file ->
                    file.canonicalPath.contains(extension.filter)
                }
        }
        classPathResolver = { gatherClassPaths(project, variantNameCapitalized) }
        bootClassPath = project.files(androidJarPath)
        outputPath = postWeaveDir.get().asFile
        logPath = project.layout.buildDirectory.file(Values.AOP_LOG).get().asFile

        val dependencies = when {
            ((javaCompileProvider != null) && (kotlinCompileProvider != null)) -> {
                arrayOf(javaCompileProvider, kotlinCompileProvider)
            }
            (javaCompileProvider != null) -> arrayOf(javaCompileProvider)
            (kotlinCompileProvider != null) -> arrayOf(kotlinCompileProvider)
            else -> throw RuntimeException("Either Java, Kotlin, or both types of compile task must be present.")
        }

        dependsOn(dependencies)
    }
}
