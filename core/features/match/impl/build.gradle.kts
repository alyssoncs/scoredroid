plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    implementation(projects.core.features.match.public)
    implementation(projects.core.infra.public)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)

    testImplementation(testFixtures(projects.core.infra.public))
}
