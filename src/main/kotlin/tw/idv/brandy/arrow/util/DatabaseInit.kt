package tw.idv.brandy.arrow.util

import com.mongodb.client.MongoClient
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import io.quarkus.redis.client.reactive.ReactiveRedisClient

@ApplicationScoped
class DatabaseInit(val quarkusMongo: MongoClient, val reactiveRedisClient: ReactiveRedisClient) {

    companion object {
        lateinit var mongoClient: MongoClient
        lateinit var redisClient: ReactiveRedisClient
    }

    fun startup(@Observes event: StartupEvent) {
        mongoClient = quarkusMongo
        redisClient = reactiveRedisClient
    }


}