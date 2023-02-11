import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.scoredroid.utils.getVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("io.gitlab.arturbosch.detekt")
}

val catalog = project.versionCatalog
with(project.extensions.getByType(KotlinJvmProjectExtension::class.java)) {
    jvmToolchain(jdkVersion = project.versionCatalog.getVersion("java").toInt())
}
