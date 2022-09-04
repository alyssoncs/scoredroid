plugins {
    id("org.scoredroid.java-module")
}

dependencies {
    api(projects.core.features.common.infra.public)
    implementation(projects.core.features.common.infra.dependencies)
    implementation(projects.core.features.common.utils)

    implementation(libs.coroutines.core)
    implementation(libs.test.truth)
}