plugins {
    alias(libs.plugins.scoredroid.kotlin.module)
    id("org.scoredroid.kotlin-module-junit5-setup")
    id("org.scoredroid.unit-test")
}

dependencies {
    api(projects.core.infra.matchRepository.dataSources.public)
}
