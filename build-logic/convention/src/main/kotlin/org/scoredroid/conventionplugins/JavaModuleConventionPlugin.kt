package org.scoredroid.conventionplugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.internal.DefaultJavaPluginExtension
import org.gradle.api.tasks.testing.Test

class JavaModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        with(project.pluginManager) {
            apply("java-library")
            apply(libs.findPlugin("kotlin.jvm").get().get().pluginId)
            apply(libs.findPlugin("detekt").get().get().pluginId)
        }

        with(project.extensions.getByType(JavaPluginExtension::class.java)) {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}