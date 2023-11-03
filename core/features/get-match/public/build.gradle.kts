plugins {
    id("org.scoredroid.feature-public")
}

dependencies {
    api(projects.core.features.common.data)
    api(libs.coroutines.core)
}
