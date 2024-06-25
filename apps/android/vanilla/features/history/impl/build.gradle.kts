plugins {
    alias(libs.plugins.scoredroid.android.feature.impl)
}

android {
    namespace = "org.scoredroid.history.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.history.public)
    api(projects.apps.android.vanilla.features.editMatch.public)
    api(projects.apps.android.vanilla.features.createMatch.public)
    api(projects.apps.android.vanilla.features.play.public)

    implementation(projects.apps.android.vanilla.common.ui.components)
    implementation(projects.core.features.getMatch.public)
    implementation(projects.core.features.removeMatch.public)

    implementation(projects.apps.android.common.utils.fragments.transactions)

    implementation(libs.immutable.collections)

    testImplementation(testFixtures(projects.core.features.getMatch.public))
    testImplementation(testFixtures(projects.core.features.removeMatch.public))
}
