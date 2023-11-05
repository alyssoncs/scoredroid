plugins {
    id("org.scoredroid.feature-public")
    id("java-test-fixtures")
}

dependencies {
    api(projects.core.features.common.response)
}
