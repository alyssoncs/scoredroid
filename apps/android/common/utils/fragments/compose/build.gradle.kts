plugins {
    alias(libs.plugins.scoredroid.android.lib)
    id("org.scoredroid.enable-compose")
}

android {
    namespace = "org.scoredroid.fragment.compose"
}

dependencies {
    api(libs.fragment.ktx)
    api(libs.compose.ui)
}
