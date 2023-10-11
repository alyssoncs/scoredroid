plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.enable-compose")
}

android {
    namespace = "org.scoredroid.ui.tooling"
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
}
