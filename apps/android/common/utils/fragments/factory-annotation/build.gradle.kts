plugins {
    alias(libs.plugins.scoredroid.android.lib)
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.fragment.factory.annotation"
}

dependencies {
    api(libs.fragment.ktx)
}