plugins {
    id("org.scoredroid.public-feature")
}

dependencies {
    api(projects.core.features.common.data)

    implementation(libs.coroutines.core)
}