plugins {
    alias(libs.plugins.scoredroid.feature.entrypoint)
}

dependencies {
    api(projects.core.features.createMatch.public)
    api(projects.core.infra.matchRepository.dataSources.public)

    implementation(projects.core.features.createMatch.impl)
    implementation(projects.core.infra.matchRepository.entrypoint)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
