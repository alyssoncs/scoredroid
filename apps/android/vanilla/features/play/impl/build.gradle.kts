plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
    id("org.scoredroid.kotlin-module-junit5-setup")
    id("org.scoredroid.unit-test")
}

android {
    namespace = "org.scoredroid.play.impl"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(projects.apps.android.vanilla.features.play.public)
    api(projects.apps.android.common.utils.fragments.factoryAnnotation)
    api(projects.apps.android.common.utils.viewmodels.factoryAnnotation)

    implementation(projects.core.features.match.public)
    implementation(projects.core.features.score.public)
    implementation(projects.apps.android.vanilla.common.ui.theme)
    implementation(projects.apps.android.vanilla.common.ui.components)

    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.apps.android.common.utils.viewmodels.test)
}
