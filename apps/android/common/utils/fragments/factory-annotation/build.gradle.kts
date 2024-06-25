plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.dagger)
}

android {
    namespace = "org.scoredroid.fragment.factory.annotation"
}

dependencies {
    api(libs.fragment.ktx)
}