plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.history.impl"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
}

dependencies {
    api(projects.apps.android.common.utils.fragments.factoryannotation)
    api(projects.apps.android.common.utils.viewmodels.factoryannotation)

    implementation(projects.core.features.match.public)

    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}