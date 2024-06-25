plugins {
    alias(libs.plugins.scoredroid.kotlin.module)
    alias(libs.plugins.scoredroid.kotlin.module.junit5.setup)
    alias(libs.plugins.scoredroid.unit.test)
}

dependencies {
    api(projects.core.infra.matchRepository.dataSources.public)
}
