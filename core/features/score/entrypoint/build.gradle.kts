plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.score.public)
    api(projects.core.infra.dependencies.public)
    implementation(projects.core.features.score.impl)
    implementation(projects.core.infra.entrypoint)
    testImplementation(projects.core.infra.test)
}
