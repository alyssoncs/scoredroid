plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.domain)
    api(projects.core.infra.dependencies.public)

    implementation(libs.coroutines.core)
}
