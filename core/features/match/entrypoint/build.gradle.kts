plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.match.public)
    api(projects.core.infra.dependencies.public)
    implementation(projects.core.features.match.impl)
    implementation(projects.core.infra.entrypoint)
    testImplementation(testFixtures(projects.core.infra.public))
}
