package org.scoredroid.conventionplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

class JavaModuleJunit5ConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.withType(Test::class.java) { testTask ->
            testTask.useJUnitPlatform()
            testTask.testLogging {
                it.exceptionFormat = FULL
                it.events = setOf(SKIPPED, PASSED, FAILED)
                it.showStandardStreams = true
            }
        }
    }
}