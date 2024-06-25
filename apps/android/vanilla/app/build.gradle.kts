plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.scoredroid.dagger)
    id("org.scoredroid.detekt")
    alias(libs.plugins.compose.compiler)
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.features.createMatch.entrypoint)
    implementation(projects.core.features.editMatch.entrypoint)
    implementation(projects.core.features.getMatch.entrypoint)
    implementation(projects.core.features.persistMatchChanges.entrypoint)
    implementation(projects.core.features.playMatch.entrypoint)
    implementation(projects.core.features.removeMatch.entrypoint)

    implementation(projects.apps.android.vanilla.features.history.impl)
    implementation(projects.apps.android.vanilla.features.createMatch.impl)
    implementation(projects.apps.android.vanilla.features.editMatch.impl)
    implementation(projects.apps.android.vanilla.features.play.impl)

    implementation(projects.apps.android.common.infra.databaseMatchDataSource)
    implementation(projects.apps.android.common.utils.fragments.fragmentFactory)
    implementation(projects.apps.android.common.utils.fragments.transactions)
    implementation(projects.apps.android.common.utils.viewmodels.vmFactory)

    implementation(libs.androidx.appcompat)
    implementation(libs.compose.material3)
    implementation(libs.fragment.ktx)
    implementation(libs.material)
    debugImplementation(libs.compose.ui.tooling)
}
