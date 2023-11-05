plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    api(projects.core.features.playMatch.public)

    implementation(projects.core.infra.matchRepository.public)
    implementation(projects.core.features.common.mapper)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
