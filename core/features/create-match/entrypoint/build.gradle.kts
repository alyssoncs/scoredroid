plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.createMatch.public)
    api(projects.core.infra.dependencies.public)

    implementation(projects.core.features.createMatch.impl)
    implementation(projects.core.infra.entrypoint)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
