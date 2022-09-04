plugins {
    id("org.scoredroid.java-module")
}

dependencies {
    api(projects.core.features.common.domain)
    api(projects.core.features.common.infra.commonmodels)
}
