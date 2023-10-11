import org.gradle.kotlin.dsl.dependencies
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
    id("org.scoredroid.enable-compose")
}

val catalog = project.versionCatalog
dependencies {
    implementation(catalog.getLibrary("compose.material3"))
    implementation(catalog.getLibrary("compose.ui.tooling.preview"))
    debugImplementation(catalog.getLibrary("compose.ui.tooling"))
}
