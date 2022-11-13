plugins {
    id("org.scoredroid.java-module")
    id("org.scoredroid.kotlin-module-junit5-setup")
    id("org.scoredroid.unit-test")
}

dependencies {
    api(projects.core.features.common.infra.dependencies.public)
}
