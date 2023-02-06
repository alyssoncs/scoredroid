package org.scoredroid.conventionplugins.android.moduletypes

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        val javaVersion = 11
        project.extensions.configure<LibraryExtension> {
            compileSdk = 33
            defaultConfig {
                minSdk = 25
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.toVersion(javaVersion)
                targetCompatibility = JavaVersion.toVersion(javaVersion)
            }
        }

        project.kotlin {
            jvmToolchain(javaVersion)
        }
    }

    private fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>) {
        (this as ExtensionAware).extensions.configure("kotlin", configure)
    }
}