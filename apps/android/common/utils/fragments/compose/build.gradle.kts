plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.enable.compose)
}

android {
    namespace = "org.scoredroid.fragment.compose"
}

dependencies {
    api(libs.fragment.ktx)
    api(libs.compose.ui)
}
