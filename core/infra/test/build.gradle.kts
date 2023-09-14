plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.infra.public)
    api(testFixtures(projects.core.infra.public))
    implementation(projects.core.infra.dependencies.impl)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)
    implementation(libs.test.junit.api)
}
