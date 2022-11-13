plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.scoredroid.dagger")
}

android {
    namespace = "org.scoredroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "org.scoredroid"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.features.match.entrypoint)
    implementation(projects.core.features.teams.entrypoint)
    implementation(projects.core.features.score.entrypoint)

    implementation(projects.apps.android.common.infra.dependencies.impl)
    implementation(projects.apps.android.common.utils.fragments.fragmentfactory)
    implementation(projects.apps.android.common.utils.fragments.factoryannotation)

    implementation(libs.androidx.appcompat)
    implementation(libs.compose.material3)
    implementation(libs.fragment.ktx)
    implementation (libs.material)
    debugImplementation(libs.compose.ui.tooling)
}