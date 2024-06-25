plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.dagger)
}

android {
    namespace = "org.scoredroid.viewmodels.factory.annotation"
}

dependencies {
    api(libs.viewmodel.ktx)
    api(libs.viewmodel.savedstate)
}