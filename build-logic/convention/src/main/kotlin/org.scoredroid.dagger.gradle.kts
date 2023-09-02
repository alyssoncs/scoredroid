import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    val catalog = project.versionCatalog
    "implementation"(catalog.getLibrary("dagger"))
    ksp(catalog.getLibrary("dagger.compiler"))
}
