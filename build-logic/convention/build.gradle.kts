plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
}

group = "org.scoredroid.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins {
        register("dagger") {
            id = "org.scoredroid.dagger"
            implementationClass = "org.scoredroid.conventionplugins.DaggerConventionPlugin"
        }
        register("kotlinModule") {
            id = "org.scoredroid.kotlin-module"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.basic.KotlinModuleConventionPlugin"
        }
        register("kotlinModuleJunit5") {
            id = "org.scoredroid.kotlin-module-junit5-setup"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.basic.KotlinModuleJunit5SetupConventionPlugin"
        }
        register("unitTest") {
            id = "org.scoredroid.unit-test"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.basic.UnitTestConventionPlugin"
        }
        register("featureEntryPoint") {
            id = "org.scoredroid.feature-entrypoint"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.moduletypes.FeatureEntrypointConventionPlugin"
        }
        register("featureImpl") {
            id = "org.scoredroid.feature-impl"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.moduletypes.FeatureImplConventionPlugin"
        }
        register("featurePublic") {
            id = "org.scoredroid.feature-public"
            implementationClass = "org.scoredroid.conventionplugins.kotlin.moduletypes.FeaturePublicConventionPlugin"
        }
    }
}
