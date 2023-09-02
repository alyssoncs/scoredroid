plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.android-compose")
}

android {
    namespace = "org.scoredroid.ui.theme"
}

dependencies {
    libs.versions.compose
    implementation(libs.androidx.core.ktx)
}
