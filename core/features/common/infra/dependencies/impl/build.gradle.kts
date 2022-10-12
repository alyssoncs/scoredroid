plugins {
    id("org.scoredroid.java-module")
    id("org.scoredroid.java-module-junit5")
    id("org.scoredroid.unit-test")
}

dependencies {
    api(projects.core.features.common.infra.dependencies.public)
}
