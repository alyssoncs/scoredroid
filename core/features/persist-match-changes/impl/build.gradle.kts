plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    api(projects.core.features.persistMatchChanges.public)

    implementation(projects.core.infra.matchRepository.public)

    testImplementation(testFixtures(projects.core.infra.matchRepository.public))
}
