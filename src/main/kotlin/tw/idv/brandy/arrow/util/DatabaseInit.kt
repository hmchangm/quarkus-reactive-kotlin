package tw.idv.brandy.arrow.util

import com.mongodb.client.MongoClient
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes


@ApplicationScoped
class DatabaseInit(val quarkusMongo: MongoClient) {

    companion object {
        lateinit var mongoClient: MongoClient
    }

    fun startup(@Observes event: StartupEvent) {
        mongoClient = quarkusMongo
    }


}
