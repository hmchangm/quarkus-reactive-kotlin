package tw.idv.brandy.arrow.rest

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class MockPostDatabase : QuarkusTestResourceLifecycleManager {

    private val db = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
        withDatabaseName("quarkus_test")
        withUsername("quarkus_test")
        withPassword("quarkus_test")
        withInitScript("init.sql")
    }

    override fun start(): MutableMap<String, String> {
        println("STARTING PostgreSQL")
        db.start()
        return mutableMapOf(Pair("quarkus.datasource.reactive.url", "postgresql://localhost:${db.firstMappedPort}/quarkus_test"))
    }

    override fun stop() {
        println("STOPPING PostgreSQL")
        db.stop()
    }
}
