plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.removeMatch.public)
    api(projects.core.infra.dependencies.public)

    implementation(projects.core.features.removeMatch.impl)
    implementation(projects.core.infra.entrypoint)

    testImplementation(testFixtures(projects.core.infra.public))
}
