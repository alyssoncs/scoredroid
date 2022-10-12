plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.score.public)
    api(projects.core.features.common.infra.dependencies.public)
    implementation(projects.core.features.score.impl)
    implementation(projects.core.features.common.infra.entrypoint)
    testImplementation(projects.core.features.common.infra.test)
}
