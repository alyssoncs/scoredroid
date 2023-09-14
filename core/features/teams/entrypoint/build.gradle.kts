plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.teams.public)
    api(projects.core.infra.dependencies.public)
    implementation(projects.core.features.teams.impl)
    implementation(projects.core.infra.entrypoint)
    testImplementation(testFixtures(projects.core.infra.public))
}
