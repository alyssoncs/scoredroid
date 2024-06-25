plugins {
    alias(libs.plugins.scoredroid.kotlin.module)
}

dependencies {
    api(projects.core.entities)
    api(projects.core.features.common.response)
}
