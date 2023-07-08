plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.features.common.data)
    api(projects.core.features.common.domain)
    api(projects.core.features.common.infra.models)
    api(projects.core.features.common.infra.dependencies.public)

    implementation(libs.coroutines.core)
}
