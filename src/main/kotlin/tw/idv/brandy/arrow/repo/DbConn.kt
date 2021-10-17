package tw.idv.brandy.arrow.repo

import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject

@ApplicationScoped
class DbConn{

    @Inject
    lateinit var client: PgPool

    companion object {
        lateinit var dbPool: PgPool
    }

    fun onStart(@Observes ev: StartupEvent?) {
        dbPool=client
        client.query("DROP TABLE IF EXISTS fruits").execute()
            .flatMap { _ -> client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Orange')").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Pear')").execute() }
            .flatMap { _ -> client.query("INSERT INTO fruits (name) VALUES ('Apple')").execute() }
            .await().indefinitely()

    }

    fun onStop(@Observes ev: ShutdownEvent?) {

    }

}