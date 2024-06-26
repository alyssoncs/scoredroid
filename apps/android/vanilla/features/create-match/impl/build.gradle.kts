plugins {
    alias(libs.plugins.scoredroid.android.feature.impl)
}

android {
    namespace = "org.scoredroid.creatematch.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.createMatch.public)

    implementation(projects.core.features.createMatch.public)
    implementation(projects.apps.android.vanilla.common.ui.components)

    testImplementation(testFixtures(projects.core.features.createMatch.public))
}
