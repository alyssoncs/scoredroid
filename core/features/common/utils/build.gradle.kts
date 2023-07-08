plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.domain)
    api(projects.core.features.common.data)
}
