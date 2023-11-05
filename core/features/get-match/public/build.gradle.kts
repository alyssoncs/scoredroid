plugins {
    id("org.scoredroid.feature-public")
}

dependencies {
    api(projects.core.features.common.response)
    api(libs.coroutines.core)
}
