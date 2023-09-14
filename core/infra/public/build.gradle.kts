plugins {
    id("org.scoredroid.kotlin-module")
    id("java-test-fixtures")
}

dependencies {
    api(projects.core.domain)
    api(projects.core.infra.dependencies.public)
    api(libs.coroutines.core)

    testFixturesImplementation(projects.core.features.common.utils)
    testFixturesImplementation(projects.core.infra.dependencies.impl)
}
