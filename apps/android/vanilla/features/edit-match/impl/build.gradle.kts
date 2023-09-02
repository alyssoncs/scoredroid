plugins {
    id("org.scoredroid.android-feature-impl")
}

android {
    namespace = "org.scoredroid.editmatch.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.editMatch.public)

    implementation(projects.core.features.match.public)
    implementation(projects.core.features.teams.public)
    implementation(projects.apps.android.vanilla.common.ui.components)
}
