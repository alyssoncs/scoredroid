plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.dagger)
}

android {
    namespace = "org.scoredroid.viewmodels.factory"
}

dependencies {
    api(projects.scoredroid.apps.android.common.utils.viewmodels.factoryAnnotation)
}