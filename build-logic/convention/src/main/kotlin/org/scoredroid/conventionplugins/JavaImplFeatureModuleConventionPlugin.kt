package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaImplFeatureModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.scoredroid.java-module")
            apply("org.scoredroid.kotlin-module-junit5")
            apply("org.scoredroid.unit-test")
        }
    }
}