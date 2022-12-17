plugins {
    id("org.scoredroid.android-lib")
}

android {
    namespace = "org.scoredroid.viewmodels.test"
}

dependencies {
    api(libs.viewmodel.ktx)
    api(libs.test.junit.api)
    implementation(libs.coroutines.core)
    implementation(libs.test.coroutines)
}