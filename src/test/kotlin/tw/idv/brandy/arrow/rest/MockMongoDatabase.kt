package tw.idv.brandy.arrow.rest

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

class MockMongoDatabase : QuarkusTestResourceLifecycleManager {

    private val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).apply {

    }

    override fun start(): MutableMap<String, String> {
        println("STARTING MONGO")
        mongoDBContainer.start()

        return mutableMapOf(Pair("quarkus.mongodb.connection-string",mongoDBContainer.replicaSetUrl))
    }

    override fun stop() {
        println("STOPPING MONGO")
        mongoDBContainer.stop()
    }
}
