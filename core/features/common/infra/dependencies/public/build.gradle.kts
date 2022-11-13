plugins {
    id("org.scoredroid.kotlin-module")
}

dependencies {
    api(projects.core.features.common.domain)
    api(projects.core.features.common.infra.commonmodels)
}
