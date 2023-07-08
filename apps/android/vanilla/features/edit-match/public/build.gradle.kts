plugins {
    id("org.scoredroid.android-lib")
}

android {
    namespace = "org.scoredroid.editmatch.api"
}

dependencies {
    implementation(libs.fragment.ktx)
}