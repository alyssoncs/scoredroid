plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.android-compose")
}

android {
    namespace = "org.scoredroid.ui.components"
}

dependencies {
    api(libs.immutable.collections)
}
