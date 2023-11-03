plugins {
    id("org.scoredroid.kotlin-module")
    id("java-test-fixtures")
}

dependencies {
    api(projects.core.entities)
    api(projects.core.infra.dependencies.public)
    api(libs.coroutines.core)

    testFixturesImplementation(projects.core.features.common.utils)
    testFixturesImplementation(projects.core.infra.dependencies.impl)
    testFixturesImplementation(libs.test.junit.api)
}
