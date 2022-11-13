plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.viewmodels.factory"
}

dependencies {
    api(libs.viewmodel.ktx)
}