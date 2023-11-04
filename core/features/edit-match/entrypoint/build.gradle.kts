plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.editMatch.public)
    api(projects.core.infra.matchRepository.dataSources.public)

    implementation(projects.core.features.editMatch.impl)
    implementation(projects.core.infra.matchRepository.entrypoint)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
