plugins {
    alias(libs.plugins.scoredroid.feature.impl)
}

dependencies {
    api(projects.core.features.getMatch.public)

    implementation(projects.core.infra.matchRepository.public)
    implementation(projects.core.features.common.mapper)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
