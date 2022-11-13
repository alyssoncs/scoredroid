plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.features.common.infra.public)
    implementation(projects.core.features.common.infra.dependencies.impl)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)
    implementation(libs.test.junit.api)
}
