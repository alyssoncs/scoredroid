plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.android-compose")
}

android {
    namespace = "org.scoredroid.ui.theme"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
