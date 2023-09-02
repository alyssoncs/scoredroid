plugins {
    id("org.scoredroid.android-feature-impl")
}

android {
    namespace = "org.scoredroid.history.impl"
}

dependencies {
    api(projects.apps.android.vanilla.features.editMatch.public)
    api(projects.apps.android.vanilla.features.createMatch.public)

    implementation(projects.core.features.match.public)

    implementation(projects.apps.android.common.utils.fragments.transactions)

    implementation(libs.immutable.collections)
}
