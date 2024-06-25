plugins {
    alias(libs.plugins.scoredroid.feature.impl)
}

dependencies {
    api(projects.core.features.removeMatch.public)

    implementation(projects.core.infra.matchRepository.public)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
