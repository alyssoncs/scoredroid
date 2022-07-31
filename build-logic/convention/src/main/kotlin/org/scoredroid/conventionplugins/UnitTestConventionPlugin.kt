package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

class UnitTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val libs = project.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

        project.dependencies.add("testRuntimeOnly", libs.findLibrary("test.junit.engine").get())
        project.dependencies.add("testImplementation", libs.findBundle("unitTest").get())
    }
}