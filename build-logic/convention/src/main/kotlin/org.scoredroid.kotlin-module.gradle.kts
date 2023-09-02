import org.scoredroid.utils.getIntVersion
import org.scoredroid.utils.versionCatalog

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.scoredroid.detekt")
}

kotlin {
    jvmToolchain(jdkVersion = project.versionCatalog.getIntVersion("java"))
}
