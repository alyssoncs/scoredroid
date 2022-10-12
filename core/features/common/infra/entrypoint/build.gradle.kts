plugins {
    id("org.scoredroid.feature-entrypoint")
}

dependencies {
    api(projects.core.features.common.infra.public)
    api(projects.core.features.common.infra.dependencies.public)
    implementation(projects.core.features.common.infra.dependencies.impl)
    testImplementation(projects.core.features.common.infra.test)
}
