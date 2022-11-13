plugins {
    id("org.scoredroid.feature-public")
}

dependencies {
    api(projects.core.features.common.data)

    implementation(libs.coroutines.core)
}
