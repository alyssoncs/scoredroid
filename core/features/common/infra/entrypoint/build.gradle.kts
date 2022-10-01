plugins {
    id("org.scoredroid.feature-entrypoint")
    id("org.scoredroid.java-module-junit5")
    id("org.scoredroid.unit-test")
}

dependencies {
    api(projects.core.features.common.infra.public)
    api(projects.core.features.common.infra.dependencies)
    implementation(projects.core.features.common.infra.concretedependencies)
    testImplementation(projects.core.features.common.infra.test)
}
