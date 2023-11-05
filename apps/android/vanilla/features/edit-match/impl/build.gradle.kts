plugins {
    id("org.scoredroid.android-feature-impl")
}

android {
    namespace = "org.scoredroid.editmatch.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.editMatch.public)

    implementation(projects.core.features.createMatch.public)
    implementation(projects.core.features.editMatch.public)
    implementation(projects.core.features.getMatch.public)
    implementation(projects.core.features.persistMatchChanges.public)
    implementation(projects.apps.android.vanilla.common.ui.components)

    testImplementation(testFixtures(projects.core.features.createMatch.public))
    testImplementation(testFixtures(projects.core.features.editMatch.public))
    testImplementation(testFixtures(projects.core.features.getMatch.public))
    testImplementation(testFixtures(projects.core.features.persistMatchChanges.public))
}
