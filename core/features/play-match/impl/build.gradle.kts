plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    api(projects.core.features.playMatch.public)

    implementation(projects.core.infra.public)
    implementation(projects.core.features.common.utils)

    testImplementation(testFixtures(projects.core.infra.public))
}