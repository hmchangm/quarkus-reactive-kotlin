package tw.idv.brandy.arrow.util

import org.eclipse.microprofile.config.ConfigProvider
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class KmongoResource {

    companion object {

        private val mongoDbString: String by lazy {
            ConfigProvider.getConfig().getOptionalValue("quarkus.mongodb.connection-string", String::class.java)
                .orElse("mongodb://localhost:27017/")
        }

        val client = KMongo.createClient(mongoDbString).coroutine
        val fruitStore = client.getDatabase("fruits")
    }

}