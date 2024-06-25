plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.android.compose)
}

android {
    namespace = "org.scoredroid.ui.components"
}

dependencies {
    api(libs.immutable.collections)

    implementation(projects.apps.android.vanilla.common.ui.tooling)
    implementation(projects.apps.android.vanilla.common.ui.theme)
}
