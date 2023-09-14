plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(testFixtures(projects.core.infra.public))
}
