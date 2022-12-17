plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.viewmodels.factory.annotation"
}

dependencies {
    api(libs.viewmodel.ktx)
    api(libs.viewmodel.savedstate)
}