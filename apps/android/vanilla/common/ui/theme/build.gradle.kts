plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.android.compose)
}

android {
    namespace = "org.scoredroid.ui.theme"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
