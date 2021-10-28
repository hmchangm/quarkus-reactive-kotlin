package tw.idv.brandy.arrow.rest

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.utility.DockerImageName

class MockDatabase : QuarkusTestResourceLifecycleManager {

    private val db = MariaDBContainer<Nothing>(DockerImageName.parse("mariadb:10.6.4")).apply {
        withDatabaseName("quarkus_test")
        withPassword("quarkus_test")
        withInitScript("init.sql")
    }

    override fun start(): MutableMap<String, String> {
        println("STARTING MariaDB")
        db.start()
        return mutableMapOf(Pair("quarkus.datasource.reactive.url", "mysql://:${db.firstMappedPort}/quarkus_test"))
    }

    override fun stop() {
        println("STOPPING MariaDB")
        db.stop()
    }
}
