package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaPublicFeatureModuleConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.scoredroid.kotlin-module")
    }
}