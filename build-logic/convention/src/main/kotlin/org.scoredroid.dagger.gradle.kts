import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("kotlin-kapt")
}

dependencies {
    val catalog = project.versionCatalog
    "implementation"(catalog.getLibrary("dagger"))
    "kapt"(catalog.getLibrary("dagger.compiler"))
}
