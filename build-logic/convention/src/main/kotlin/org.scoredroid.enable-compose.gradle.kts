import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

val catalog = project.versionCatalog
android {
    buildFeatures {
        compose = true
    }
}
