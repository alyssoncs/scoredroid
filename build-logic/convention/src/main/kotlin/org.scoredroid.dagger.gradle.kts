import gradle.kotlin.dsl.accessors._0a60746abe7e8186900dfa6c83859209.implementation
import gradle.kotlin.dsl.accessors._8b7e56200444878463e419b6e3baca9b.kapt
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("kotlin-kapt")
}

dependencies {
    val catalog = project.versionCatalog
    implementation(catalog.getLibrary("dagger"))
    kapt(catalog.getLibrary("dagger.compiler"))
}
