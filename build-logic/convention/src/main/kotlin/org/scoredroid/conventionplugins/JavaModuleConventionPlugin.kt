package org.scoredroid.conventionplugins

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.scoredroid.utils.getPluginId
import org.scoredroid.utils.versionCatalog

class JavaModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val catalog = project.versionCatalog
        with(project.pluginManager) {
            apply("java-library")
            apply(catalog.getPluginId("kotlin.jvm"))
            apply(catalog.getPluginId("detekt"))
        }

        with(project.extensions.getByType(JavaPluginExtension::class.java)) {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}