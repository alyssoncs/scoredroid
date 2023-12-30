import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    config.setFrom("${rootDir}/config/detekt/detekt.yml")
    source.from("src/testFixtures/kotlin")
}

dependencies {
    val catalog = project.versionCatalog
    detektPlugins(catalog.getLibrary("detekt.formatting.plugin"))
    detektPlugins(catalog.getLibrary("detekt.compose.plugin"))
}
