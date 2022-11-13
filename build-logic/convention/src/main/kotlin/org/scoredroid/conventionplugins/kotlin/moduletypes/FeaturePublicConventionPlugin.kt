package org.scoredroid.conventionplugins.kotlin.moduletypes

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeaturePublicConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.scoredroid.kotlin-module")
    }
}