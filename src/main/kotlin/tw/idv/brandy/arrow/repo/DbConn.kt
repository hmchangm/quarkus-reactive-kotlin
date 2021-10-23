package tw.idv.brandy.arrow.repo

import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
class DbConn {

    @Inject
    lateinit var client: PgPool

    companion object {
        lateinit var dbPool: PgPool
    }

    fun onStart(@Observes ev: StartupEvent?) {
        dbPool = client
    }

    fun onStop(@Observes ev: ShutdownEvent?) {

    }

}