package tw.idv.brandy.arrow.repo

import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
class DbConn {

    @Inject
    lateinit var client: ReactiveMongoClient

    companion object {
        lateinit var dbPool: ReactiveMongoClient
    }

    fun onStart(@Observes ev: StartupEvent) {
        dbPool = client
    }

    fun onStop(@Observes ev: ShutdownEvent) {

    }

}