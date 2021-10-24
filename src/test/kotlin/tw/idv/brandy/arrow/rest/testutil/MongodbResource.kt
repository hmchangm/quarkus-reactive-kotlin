package tw.idv.brandy.arrow.rest.testutil

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.*


class PostgresResource : QuarkusTestResourceLifecycleManager {
    override fun start(): Map<String, String> {
        db.start()
        return Collections.singletonMap("quarkus.datasource.url", db.jdbcUrl)
    }

    override fun stop() {
        db.close()
    }

    companion object {
        val db = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
            withDatabaseName("quarkus_test")
            withUsername("quarkus_test")
            withPassword("quarkus_test")
            withInitScript("init.sql")
        }
    }
}