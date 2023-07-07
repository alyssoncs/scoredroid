import gradle.kotlin.dsl.accessors._0a60746abe7e8186900dfa6c83859209.testImplementation
import gradle.kotlin.dsl.accessors._0a60746abe7e8186900dfa6c83859209.testRuntimeOnly
import org.scoredroid.utils.getBundle
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

dependencies {
    val catalog = project.versionCatalog
    testRuntimeOnly(catalog.getLibrary("test.junit.engine"))
    testImplementation(catalog.getBundle("unitTest"))
}
