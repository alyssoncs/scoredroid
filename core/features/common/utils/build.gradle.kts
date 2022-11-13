plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.features.common.data)
    api(projects.core.features.common.domain)
}
