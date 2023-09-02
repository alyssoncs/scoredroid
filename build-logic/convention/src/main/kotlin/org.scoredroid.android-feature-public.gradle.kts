import org.gradle.kotlin.dsl.dependencies
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("org.scoredroid.android-lib")
}

val catalog = project.versionCatalog
dependencies {
    implementation(catalog.getLibrary("fragment.ktx"))
}
