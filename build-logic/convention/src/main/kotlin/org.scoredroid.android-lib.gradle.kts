import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.scoredroid.utils.getVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val catalog = project.versionCatalog
val javaVersion = catalog.getVersion("java").toInt()
project.extensions.configure<LibraryExtension> {
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

fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>) {
    (this as ExtensionAware).extensions.configure("kotlin", configure)
}
