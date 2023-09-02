plugins {
    id("org.scoredroid.android-lib")
}

android {
    namespace = "org.scoredroid.play.api"
}

dependencies {
    implementation(libs.fragment.ktx)
}