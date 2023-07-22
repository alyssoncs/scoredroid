import org.scoredroid.utils.getBundle
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

dependencies {
    val catalog = project.versionCatalog
    "testRuntimeOnly"(catalog.getLibrary("test.junit.engine"))
    "testImplementation"(catalog.getBundle("unitTest"))
}
