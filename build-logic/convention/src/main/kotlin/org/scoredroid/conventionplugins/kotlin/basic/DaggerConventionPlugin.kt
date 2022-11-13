package org.scoredroid.conventionplugins.kotlin.basic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

class DaggerConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("kotlin-kapt")
        }

        val catalog = project.versionCatalog
        project.dependencies.add("implementation", catalog.getLibrary("dagger"))
        project.dependencies.add("kapt", catalog.getLibrary("dagger.compiler"))
    }
}