plugins {
    `kotlin-dsl`
}

group = "org.scoredroid.buildlogic"

kotlin {
    jvmToolchain(jdkVersion = 17)
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.ksp.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.detekt.gradle)
}
