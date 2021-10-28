package tw.idv.brandy.arrow.repo

import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import io.vertx.mutiny.mysqlclient.MySQLPool
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
class DbConn {

    @Inject
    lateinit var client: MySQLPool

    companion object {
        lateinit var dbPool: MySQLPool
    }

    fun onStart(@Observes ev: StartupEvent?) {
        dbPool = client
    }

    fun onStop(@Observes ev: ShutdownEvent?) {

    }

}