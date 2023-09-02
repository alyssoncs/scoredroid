plugins {
    id("org.scoredroid.android-feature-impl")
}

android {
    namespace = "org.scoredroid.play.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.play.public)

    implementation(projects.core.features.match.public)
    implementation(projects.core.features.score.public)
}
