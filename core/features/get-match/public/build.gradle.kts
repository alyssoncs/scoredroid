plugins {
    id("org.scoredroid.feature-public")
    alias(libs.plugins.test.fixtures)
}

dependencies {
    api(projects.core.features.common.response)
    api(libs.coroutines.core)
}
