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
        register("javaModuleJunit5") {
            id = "org.scoredroid.java-module-junit5"
            implementationClass = "org.scoredroid.conventionplugins.JavaModuleJunit5ConventionPlugin"
        }
        register("javaModule") {
            id = "org.scoredroid.java-module"
            implementationClass = "org.scoredroid.conventionplugins.JavaModuleConventionPlugin"
        }
        register("publicFeature") {
            id = "org.scoredroid.public-feature"
            implementationClass = "org.scoredroid.conventionplugins.JavaPublicFeatureModuleConventionPlugin"
        }
        register("implFeature") {
            id = "org.scoredroid.impl-feature"
            implementationClass = "org.scoredroid.conventionplugins.JavaImplFeatureModuleConventionPlugin"
        }
        register("unitTest") {
            id = "org.scoredroid.unit-test"
            implementationClass = "org.scoredroid.conventionplugins.UnitTestConventionPlugin"
        }
        register("featureEntryPoint") {
            id = "org.scoredroid.feature-entry-point"
            implementationClass = "org.scoredroid.conventionplugins.FeatureEntryPointConventionPlugin"
        }
    }
}
