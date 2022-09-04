plugins {
    id("org.scoredroid.java-module")
}

dependencies {
    api(projects.core.features.common.data)
    api(projects.core.features.common.domain)
    api(projects.core.features.common.infra.commonmodels)
    implementation(projects.core.features.common.infra.dependencies)

    implementation(libs.coroutines.core)
}
