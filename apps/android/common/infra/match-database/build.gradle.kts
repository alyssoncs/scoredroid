plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.dagger")
    id("com.google.devtools.ksp")
}

android {
    namespace = "org.scoredroid.infra.db"

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    ksp(libs.room.compiler)
    implementation(libs.bundles.room)

    implementation(libs.coroutines.core)
}
