package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.scoredroid.utils.versionCatalog

class UnitTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val catalog = project.versionCatalog

        project.dependencies.add("testRuntimeOnly", catalog.findLibrary("test.junit.engine").get())
        project.dependencies.add("testImplementation", catalog.findBundle("unitTest").get())
    }
}