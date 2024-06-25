plugins {
    alias(libs.plugins.scoredroid.feature.entrypoint)
}

dependencies {
    api(projects.core.infra.matchRepository.public)

    implementation(projects.core.infra.matchRepository.dataSources.impl)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
