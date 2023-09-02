import org.gradle.kotlin.dsl.dependencies
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.getVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
}

val catalog = project.versionCatalog
android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = catalog.getVersion("compose.compiler")
    }
}

dependencies {
    implementation(catalog.getLibrary("compose.material3"))
    implementation(catalog.getLibrary("compose.ui.tooling.preview"))
    debugImplementation(catalog.getLibrary("compose.ui.tooling"))
}
