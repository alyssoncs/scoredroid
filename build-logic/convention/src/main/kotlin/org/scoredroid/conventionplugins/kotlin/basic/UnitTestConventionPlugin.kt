package org.scoredroid.conventionplugins.kotlin.basic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.scoredroid.utils.getBundle
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

class UnitTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val catalog = project.versionCatalog

        project.dependencies.add("testRuntimeOnly", catalog.getLibrary("test.junit.engine"))
        project.dependencies.add("testImplementation", catalog.getBundle("unitTest"))
    }
}