plugins {
    id("org.scoredroid.feature-entry-point")
}

dependencies {
    api(projects.core.features.match.public)
    api(projects.core.features.common.infra.dependencies)
    implementation(projects.core.features.match.impl)
    implementation(projects.core.features.common.infra.entrypoint)
}
