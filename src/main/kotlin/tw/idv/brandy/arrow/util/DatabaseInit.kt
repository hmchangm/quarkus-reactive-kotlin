package tw.idv.brandy.arrow.util

import arrow.core.zip
import arrow.typeclasses.Semigroup
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

        QuarkusConfig.read("quarkus.package.type").zip(
            Semigroup.nonEmptyList(),
            QuarkusConfig.read("myConfig.xxx"),
            QuarkusConfig.read("myConfig.xxx")
        ) { a, _, _ -> println("config 1st : $a") }.mapLeft { nel ->
            nel.map { println(it) }
            throw RuntimeException("stop for configuration missing")
        }

        mongoClient = quarkusMongo
    }


}