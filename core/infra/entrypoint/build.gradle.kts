plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.infra.public)
    implementation(projects.core.infra.dependencies.impl)
    testImplementation(testFixtures(projects.core.infra.public))
}
