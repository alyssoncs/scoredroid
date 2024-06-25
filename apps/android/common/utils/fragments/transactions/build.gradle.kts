plugins {
    alias(libs.plugins.scoredroid.android.lib)
}

android {
    namespace = "org.scoredroid.fragment.factory.transactions"
}

dependencies {
    api(libs.fragment.ktx)
}