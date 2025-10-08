package com.crescenzi.jintonic.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.crescenzi.jintonic.gradle.core.Values
import com.crescenzi.jintonic.gradle.core.Values.AOP_WEAVE_EXTENSION
import com.crescenzi.jintonic.gradle.core.configureAopWeaveTask
import com.crescenzi.jintonic.gradle.extension.*
import com.crescenzi.jintonic.gradle.tasks.MainTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


/**
 * Jintonic Gradle Plugin for AOP weaving via Android's bytecode pipeline.
 *
 * Steps:
 * 1. Redirect Kotlin/Java compile outputs ( '/javac/<variant>/compileDebugJavaWithJavac' will be empty).
 * 2. Copy compiled classes to a staging directory.
 * 3. Perform AOP weaving on combined classes.
 * 4. Register as a bytecode generator so Android treats it as a formal build step.
 */

@Suppress("unused")
class JinPlugin : Plugin<Project> {


    override fun apply(project: Project) {
        val isAndroid = project.plugins.hasPlugin(AppPlugin::class.java)
        val isLibrary = project.plugins.hasPlugin(LibraryPlugin::class.java)
        val isDynamicLibrary = project.plugins.hasPlugin(DynamicFeaturePlugin::class.java)

        if (!isAndroid && !isLibrary && !isDynamicLibrary) {
            throw GradleException("'com.android.application' or 'com.android.library' plugin required.")
        }

        /**
         * SET "-Xsam-conversions=class" FLAG (Compiler)
         */
        var i=0
        project.afterEvaluate {
            project.tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions.freeCompilerArgs += Values.ADDITIONAL_COMPILER_FLAG_1
                if(i == 0) project.jintonicLog("Applied ${Values.ADDITIONAL_COMPILER_FLAG_1} to your '${project.name}' module")
                i++
            }
        }


        val extension = project.extensions.create(AOP_WEAVE_EXTENSION, AopWeaveExtension::class.java)
        val android = project.extensions.findByName(Values.ANDROID_EXTENSION_NAME) as BaseExtension
        project.afterEvaluate {
            val variants = if (isAndroid or isDynamicLibrary) {
                (android as AppExtension).applicationVariants
            } else {
                (android as LibraryExtension).libraryVariants
            }

            variants.forEach { variant ->
                // ==== Strings in different forms for directories and task names ==== //
                val javaLangLowercase = Values.LANG_JAVA
                val kotlinLangLowercase = Values.LANG_KOTLIN

                // ==== Variant names in different cases ==== //
                val variantNameLowercase = variant.name
                val variantNameCapitalized = variant.name.capitalized()

                // ==== Directories for pre-weaving Kotlin and Java classes ==== //
                val preWeaveJavaDir = project.layout
                    .buildDirectory
                    .dir(Values.PRE_WEAVE_DIR_TEMPLATE.format(variantNameLowercase, javaLangLowercase))
                val preWeaveKotlinDir = project.layout
                    .buildDirectory
                    .dir(Values.PRE_WEAVE_DIR_TEMPLATE.format(variantNameLowercase, kotlinLangLowercase))

                // ==== Directory for post-weave output ==== //
                val postWeaveDir = project.layout
                    .buildDirectory
                    .dir(Values.POST_WEAVE_DIR_TEMPLATE.format(variantNameLowercase))
                project.jintonicLog("output dir ${postWeaveDir.get().asFile.absolutePath}")

                // ==== Acquire Task providers for compilation and custom weaving ==== //
                val kotlinCompileProvider = project.kotlinCompileTaskProvider(variantNameCapitalized)
                val javaCompileProvider = project.javaCompileTaskProvider(variantNameCapitalized)
                val kaptTaskProvider = project.kaptTaskProvider(variantNameCapitalized)

                // ==== Register custom tasks ==== //
                val aopWeaveProvider = project.tasks
                    .register(Values.AOP_WEAVE_TASK.format(variantNameCapitalized), MainTask::class.java)
                val jacocoReportTaskProvider = project.jacocoReportTaskProvider(variantNameCapitalized)
                val extractAnnotationsTaskProvider = project.extractAnnotationsTaskProvider(variantNameCapitalized)

                // ==== Restore pre-weave classes for Kapt incremental builds ==== //
                if (kaptTaskProvider != null) {
                    configureKaptTask(kaptTaskProvider, kotlinCompileProvider, javaCompileProvider, preWeaveJavaDir, preWeaveKotlinDir)
                }

                // ==== Configure Kotlin and Java compilation to output to pre-weave directories ==== //
                if (kotlinCompileProvider != null) {
                    configureKotlinCompileTask(kotlinCompileProvider, preWeaveKotlinDir)
                }
                if (javaCompileProvider != null) {
                    configureJavaCompileTask(project, javaCompileProvider, preWeaveKotlinDir, preWeaveJavaDir)
                }

                // ==== Configure JacocoReport task with post-weave directory ==== //
                if (jacocoReportTaskProvider != null) {
                    configureJacocoReportTask(jacocoReportTaskProvider, postWeaveDir)
                }

                // ==== Adjust extractAnnotations task to use pre-weave Kotlin directory ==== //
                if (extractAnnotationsTaskProvider != null) {
                    configureExtractAnnotationsTask(extractAnnotationsTaskProvider, preWeaveKotlinDir)
                }

                // ==== Android SDK jar needed for weaving ==== //
                val androidJarPath = Values.ANDROID_JAR_TEMPLATE.format(
                    android.sdkDirectory.absolutePath,
                    android.compileSdkVersion
                )

                // ==== Configure AOP weaving task ==== //
                configureAopWeaveTask(
                    project = project,
                    extension = extension,
                    variantNameCapitalized = variantNameCapitalized,
                    javaCompileProvider = javaCompileProvider,
                    kotlinCompileProvider = kotlinCompileProvider,
                    aopWeaveProvider = aopWeaveProvider,
                    androidJarPath = androidJarPath,
                    preWeaveJavaDir = preWeaveJavaDir,
                    preWeaveKotlinDir = preWeaveKotlinDir,
                    postWeaveDir = postWeaveDir
                )

                // ==== Register modified bytecode with Android build pipeline ==== //
                variant.registerPostJavacGeneratedBytecode(project.files(aopWeaveProvider.map { it.outputPath!! }))
            }
        }

    }



}
open class AopWeaveExtension {
    var filter = ""
}