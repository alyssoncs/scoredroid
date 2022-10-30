plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.scoredroid.java-module-junit5")
    alias(libs.plugins.ksp)
}

android {
    namespace = "org.scoredroid.infra"
    compileSdk = 33

    defaultConfig {
        minSdk = 25
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(projects.core.features.common.infra.dependencies.public)

    ksp(libs.room.compiler)
    api(libs.bundles.room)

    implementation(libs.coroutines.core)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(libs.test.coroutines)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}