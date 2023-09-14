plugins {
    id("org.scoredroid.kotlin-module")
    id("java-test-fixtures")
}

dependencies {
    api(projects.core.domain)
    api(projects.core.infra.dependencies.public)

    implementation(libs.coroutines.core)
}
