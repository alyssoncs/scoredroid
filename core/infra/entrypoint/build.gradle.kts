plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.infra.matchRepository.public)

    implementation(projects.core.infra.dependencies.impl)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
