@file:Suppress("UNUSED")
package com.crescenzi.jintonic.gradle.tasks

import com.crescenzi.jintonic.gradle.core.Values
import com.crescenzi.jintonic.gradle.executor.AspectPath
import com.crescenzi.jintonic.gradle.executor.BootClassPath
import com.crescenzi.jintonic.gradle.executor.ClassPath
import com.crescenzi.jintonic.gradle.executor.Executor
import com.crescenzi.jintonic.gradle.executor.LogPath
import com.crescenzi.jintonic.gradle.executor.OutputPath
import com.crescenzi.jintonic.gradle.executor.TargetPath
import com.crescenzi.jintonic.gradle.extension.jintonicLog
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class MainTask : DefaultTask() {

    /**
     *  Using this as a Gradle cache busting strategy. Increment this value if needed to prevent an older weave cache
     *  entry from getting picked up, despite new logic existing.
    **/
    @Input
    var version: Long = 1L

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    var targetPath: FileCollection? = null

    @Internal
    var aspectPathResolver: () -> FileCollection? = { null }

    @Classpath
    var aspectPath: FileCollection? = null
        get() = aspectPathResolver()
        set(value) { field = value }

    @Internal
    var classPathResolver: () -> FileCollection? = { null }

    @Classpath
    var classPath: FileCollection? = null
        get() = classPathResolver()
        set(value) { field = value }

    @Classpath
    var bootClassPath: FileCollection? = null

    @OutputDirectory
    var outputPath: File? = null

    @Internal
    var logPath: File? = null

    @TaskAction
    fun weave() {
        if (targetPath!!.isEmpty) {
            project.jintonicLog("No classes found. Will skip.")
            return
        }

        Executor(
            targetPath = TargetPath(targetPath!!.asPath),
            aspectPath = AspectPath(aspectPath!!.asPath),
            outputPath = OutputPath(outputPath!!.absolutePath),
            logPath = LogPath(logPath!!.absolutePath),
            classPath = ClassPath(classPath!!.asPath),
            bootClassPath = BootClassPath(bootClassPath!!.asPath),
            logger = logger
        ).execute()

        targetPath!!.forEach {
            copyNonClassFiles(it, outputPath!!)
        }
    }

    /**
     * Copies all non .class files over from the pre-weave directory, to the weave output directory. This makes sure
     * things like resources and the MANIFEST make it over.
     */
    private fun copyNonClassFiles(from: File, to: File) {
        if (!from.exists()) {
            return
        } else if (!from.isDirectory) {
            throw GradleException("${Values.LOG_PREFIX} Expected a directory: from=$from, to=$to")
        }

        from.listFiles { file -> file.isDirectory }
            .forEach { fromDir ->
                val toDir = File(to, fromDir.name).also { if (!it.exists()) it.mkdirs() }
                copyNonClassFiles(fromDir, toDir)
            }

        from.listFiles { file -> (!file.isDirectory && !file.name.endsWith(".class")) }
            .forEach { fromFile ->
                fromFile.copyTo(File(to, fromFile.name), true)
            }
    }
}