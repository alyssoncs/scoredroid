plugins {
    alias(libs.plugins.scoredroid.kotlin.module)
    id("java-test-fixtures")
}

dependencies {
    api(projects.core.entities)
    api(projects.core.infra.matchRepository.dataSources.public)
    api(libs.coroutines.core)

    testFixturesApi(projects.core.features.common.response)
    testFixturesImplementation(projects.core.features.common.mapper)
    testFixturesImplementation(projects.core.infra.matchRepository.dataSources.impl)
    testFixturesImplementation(libs.test.junit.api)
}
