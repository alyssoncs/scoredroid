import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("kotlin-kapt")
}

dependencies {
    val catalog = project.versionCatalog
    add("implementation", catalog.getLibrary("dagger"))
    add("kapt", catalog.getLibrary("dagger.compiler"))
}
