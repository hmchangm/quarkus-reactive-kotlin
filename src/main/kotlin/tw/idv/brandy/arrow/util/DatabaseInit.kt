package tw.idv.brandy.arrow.util

import io.quarkus.mongodb.reactive.ReactiveMongoClient
import com.mongodb.client.MongoClient
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class DatabaseInit(val client: ReactiveMongoClient, val mongo: MongoClient) {

    companion object {
        lateinit var dbPool: ReactiveMongoClient
        lateinit var mongoClient: MongoClient
    }

    fun onStart(@Observes ev: StartupEvent) {
        dbPool = client
        mongoClient = mongo
    }

    fun onStop(@Observes ev: ShutdownEvent) {

    }

}
