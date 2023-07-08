import org.scoredroid.utils.getVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.scoredroid.detekt")
}

val catalog = project.versionCatalog
val javaVersion = catalog.getVersion("java").toInt()
android {
    compileSdk = catalog.getVersion("compileSdk").toInt()
    defaultConfig {
        minSdk = catalog.getVersion("minSdk").toInt()
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
