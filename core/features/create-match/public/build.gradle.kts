plugins {
    alias(libs.plugins.scoredroid.feature.public)
    alias(libs.plugins.test.fixtures)
}

dependencies {
    api(projects.core.features.common.response)
}
