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
    api(projects.core.infra.matchRepository.dataSources.public)
    api(projects.apps.android.common.infra.matchDatabase)

    implementation(libs.coroutines.core)
    implementation(libs.bundles.room)
    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.coroutines)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.espresso.core)
}
