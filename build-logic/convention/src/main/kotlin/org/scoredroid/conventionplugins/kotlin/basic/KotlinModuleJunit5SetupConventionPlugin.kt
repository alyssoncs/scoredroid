package org.scoredroid.conventionplugins.kotlin.basic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.kotlin.dsl.withType

class KotlinModuleJunit5SetupConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.withType<Test>().configureEach {
            useJUnitPlatform()
            testLogging {
                exceptionFormat = FULL
                events = setOf(SKIPPED, PASSED, FAILED)
                showStandardStreams = true
            }
        }
    }
}