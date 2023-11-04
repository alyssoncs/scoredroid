plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    api(projects.core.features.persistMatchChanges.public)

    implementation(projects.core.infra.public)

    testImplementation(testFixtures(projects.core.infra.public))
}
