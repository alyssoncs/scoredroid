plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid.infra.impl"

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    api(projects.core.features.common.infra.dependencies.public)
    api(projects.apps.android.common.infra.dependencies.database)

    implementation(libs.coroutines.core)
    implementation(libs.bundles.room)
    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.coroutines)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.espresso.core)
}