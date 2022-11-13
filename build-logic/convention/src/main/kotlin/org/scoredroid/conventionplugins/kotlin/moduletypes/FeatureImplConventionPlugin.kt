package org.scoredroid.conventionplugins.kotlin.moduletypes

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.scoredroid.kotlin-module")
            apply("org.scoredroid.kotlin-module-junit5-setup")
            apply("org.scoredroid.unit-test")
        }
    }
}