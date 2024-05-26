plugins {
    `kotlin-dsl`
}

group = "org.scoredroid.buildlogic"

kotlin {
    jvmToolchain(jdkVersion = libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.compose.compiler.gradle)
    implementation(libs.ksp.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.detekt.gradle)
}
