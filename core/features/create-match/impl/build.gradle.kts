plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    api(projects.core.features.createMatch.public)

    implementation(projects.core.infra.matchRepository.public)
    implementation(projects.core.features.common.utils)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
