plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.common.infra.public)
    api(projects.core.features.common.infra.dependencies)
    implementation(projects.core.features.common.infra.concretedependencies)
}
