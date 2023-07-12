plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.scoredroid.dagger")
    id("org.scoredroid.detekt")
}

android {
    namespace = "org.scoredroid"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.scoredroid"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
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
                "proguard-rules.pro",
            )
        }
    }
    val javaVersion = libs.versions.java.get().toInt()
    compileOptions {
        targetCompatibility = JavaVersion.toVersion(javaVersion)
    }
    kotlin {
        jvmToolchain(javaVersion)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.features.match.entrypoint)
    implementation(projects.core.features.teams.entrypoint)
    implementation(projects.core.features.score.entrypoint)

    implementation(projects.apps.android.vanilla.features.history.impl)
    implementation(projects.apps.android.vanilla.features.createMatch.impl)
    implementation(projects.apps.android.vanilla.features.editMatch.impl)

    implementation(projects.apps.android.common.infra.dependencies.impl)
    implementation(projects.apps.android.common.utils.fragments.fragmentFactory)
    implementation(projects.apps.android.common.utils.viewmodels.vmFactory)

    implementation(libs.androidx.appcompat)
    implementation(libs.compose.material3)
    implementation(libs.fragment.ktx)
    implementation(libs.material)
    debugImplementation(libs.compose.ui.tooling)
}
