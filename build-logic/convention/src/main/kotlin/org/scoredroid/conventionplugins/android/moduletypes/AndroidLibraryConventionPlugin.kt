package org.scoredroid.conventionplugins.android.moduletypes

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        project.extensions.configure<LibraryExtension> {

            compileSdk = 33
            defaultConfig {
                minSdk = 25

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            val javaVersion = JavaVersion.VERSION_11
            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }

            kotlinOptions {
                jvmTarget = javaVersion.toString()
            }
        }
    }

    private fun LibraryExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
        (this as ExtensionAware).extensions.configure("kotlinOptions", block)
    }
}