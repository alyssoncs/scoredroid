plugins {
    id("org.scoredroid.impl-feature")
}

dependencies {
    implementation(projects.core.features.teams.public)
    implementation(projects.core.features.common.infra.public)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)

    testImplementation(projects.core.features.common.infra.test)
}
