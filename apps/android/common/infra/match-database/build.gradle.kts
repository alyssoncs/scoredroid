plugins {
    alias(libs.plugins.scoredroid.android.lib)
    alias(libs.plugins.scoredroid.dagger)
    alias(libs.plugins.ksp)
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
