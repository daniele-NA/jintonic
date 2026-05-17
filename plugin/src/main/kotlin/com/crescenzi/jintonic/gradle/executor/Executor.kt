package com.crescenzi.jintonic.gradle.executor

import com.crescenzi.jintonic.gradle.core.Values
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.logging.Logger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * A wrapper around the AspectJ weaver to make it a little easier to work with.
 */

@JvmInline
value class TargetPath(val value: String)
@JvmInline
value class AspectPath(val value: String)
@JvmInline
value class OutputPath(val value: String)
@JvmInline
value class LogPath(val value: String)
@JvmInline
value class ClassPath(val value: String)
@JvmInline
value class BootClassPath(val value: String)
class Executor(
    private val targetPath: TargetPath,
    private val aspectPath: AspectPath,
    private val outputPath: OutputPath,
    private val logPath: LogPath,
    private val classPath: ClassPath,
    private val bootClassPath: BootClassPath,
    private val logger: Logger
) {
    companion object {
        internal val lock = ReentrantLock()
    }


    fun execute() {
        val compilerArgs = arrayOf<String>(
            Values.CMD_SHOW_WEAVE_INFO,
            Values.CMD_JAVA_1_8,

            Values.CMD_IN_PATH, // Files that use our own AOP annotations.
            targetPath.value,

            Values.CMD_ASPECT_PATH, // This path should include classes that have Aspects.
            aspectPath.value,

            Values.CMD_OUTPUT_DIR, // Weave output directory.
            outputPath.value,

            Values.CMD_CLASS_PATH, // The classpath can just be the same as the one used to compile our files.
            classPath.value,

            Values.CMD_LOG, // Look for this log in <project-root>/build.
            logPath.value,

            Values.CMD_BOOT_CLASS_PATH, // DON'T CHANGE
            bootClassPath.value
        )

        /* ====
        Use a lock to prevent concurrent AspectJ runs from occurring. AspectJ does not appear to be thread safe
        Concurrency was causing random build failures as well as weaving failures
          ==== */
        lock.withLock {
            val handler = MessageHandler(true)
            Main().run(compilerArgs, handler)

            handler.getMessages(null, true).forEach {
                when (it.kind) {
                    IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> logger.error(
                        it.message,
                        it.thrown
                    )

                    IMessage.WARNING -> logger.warn(it.message, it.thrown)
                    IMessage.INFO -> logger.info(it.message, it.thrown)
                    IMessage.DEBUG -> logger.debug(it.message, it.thrown)
                }
            }
        }
    }
}