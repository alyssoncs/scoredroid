import org.scoredroid.utils.getBundle
import org.scoredroid.utils.getLibrary
import org.scoredroid.utils.versionCatalog

dependencies {
    val catalog = project.versionCatalog
    add("testRuntimeOnly", catalog.getLibrary("test.junit.engine"))
    add("testImplementation", catalog.getBundle("unitTest"))
}
