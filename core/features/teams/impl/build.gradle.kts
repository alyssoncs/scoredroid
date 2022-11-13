plugins {
    id("org.scoredroid.feature-impl")
}

dependencies {
    implementation(projects.core.features.teams.public)
    implementation(projects.core.features.common.infra.public)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)

    testImplementation(projects.core.features.common.infra.test)
}
