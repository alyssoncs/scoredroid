import org.scoredroid.utils.getIntVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.scoredroid.detekt")
}

val catalog = project.versionCatalog
val javaVersion = catalog.getIntVersion("java")
android {
    compileSdk = catalog.getIntVersion("compileSdk")
    defaultConfig {
        minSdk = catalog.getIntVersion("minSdk")
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
