plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.fragment.factory.annotation"
}

dependencies {
    api(libs.viewmodel.ktx)
}