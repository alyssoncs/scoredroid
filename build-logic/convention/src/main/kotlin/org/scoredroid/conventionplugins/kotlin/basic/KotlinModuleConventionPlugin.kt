package org.scoredroid.conventionplugins.kotlin.basic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.scoredroid.utils.getPluginId
import org.scoredroid.utils.versionCatalog

class KotlinModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val catalog = project.versionCatalog
        with(project.pluginManager) {
            apply("java-library")
            apply(catalog.getPluginId("kotlin.jvm"))
            apply(catalog.getPluginId("detekt"))
        }

        with(project.extensions.getByType(KotlinJvmProjectExtension::class.java)) {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
        }
    }
}