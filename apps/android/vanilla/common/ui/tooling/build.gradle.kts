plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.enable.compose)
}

android {
    namespace = "org.scoredroid.ui.tooling"
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
}
