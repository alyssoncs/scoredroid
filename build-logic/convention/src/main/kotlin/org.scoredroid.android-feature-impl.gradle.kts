plugins {
    id("org.scoredroid.android-lib")
    id("org.scoredroid.android-compose")
    id("org.scoredroid.dagger")
    id("org.scoredroid.kotlin-module-junit5-setup")
    id("org.scoredroid.unit-test")
}

dependencies {
    api(project(":apps:android:common:utils:fragments:compose"))
    api(project(":apps:android:common:utils:fragments:factory-annotation"))
    api(project(":apps:android:common:utils:viewmodels:factory-annotation"))

    implementation(project(":apps:android:vanilla:common:ui:theme"))
    implementation(project(":apps:android:vanilla:common:ui:tooling"))

    testImplementation(project(":apps:android:common:utils:viewmodels:test"))
}
