plugins {
    alias(libs.plugins.scoredroid.android.feature.impl)
}

android {
    namespace = "org.scoredroid.play.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.play.public)
    api(projects.apps.android.vanilla.features.editMatch.public)

    implementation(projects.apps.android.common.utils.fragments.transactions)
    implementation(projects.apps.android.vanilla.common.ui.components)
    implementation(projects.core.features.getMatch.public)
    implementation(projects.core.features.persistMatchChanges.public)
    implementation(projects.core.features.playMatch.public)

    testImplementation(testFixtures(projects.core.features.getMatch.public))
    testImplementation(testFixtures(projects.core.features.persistMatchChanges.public))
    testImplementation(testFixtures(projects.core.features.playMatch.public))
}
