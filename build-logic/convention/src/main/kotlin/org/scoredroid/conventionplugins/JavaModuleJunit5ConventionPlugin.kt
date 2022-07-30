package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class JavaModuleJunit5ConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.withType(Test::class.java) {
            it.useJUnitPlatform()
        }
    }
}