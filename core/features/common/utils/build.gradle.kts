plugins {
    id("org.scoredroid.java-module")
}

dependencies {
    api(projects.core.features.common.data)
    api(projects.core.features.common.domain)
}
