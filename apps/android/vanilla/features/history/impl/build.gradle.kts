plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
    id("org.scoredroid.kotlin-module-junit5-setup")
    id("org.scoredroid.unit-test")
}

android {
    namespace = "org.scoredroid.history.impl"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(projects.apps.android.common.utils.fragments.factoryannotation)
    api(projects.apps.android.common.utils.viewmodels.factoryannotation)
    api(projects.apps.android.vanilla.features.editMatch.public)

    implementation(projects.core.features.match.public)
    implementation(projects.apps.android.vanilla.common.ui.theme)

    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.apps.android.common.utils.viewmodels.test)
}