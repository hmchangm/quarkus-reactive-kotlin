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
        client.query("DROP TABLE IF EXISTS fruits").execute()
            .flatMap { client.query("CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL)").execute() }
            .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Kiwi')").execute() }
            .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Durian')").execute() }
            .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Pomelo')").execute() }
            .flatMap { client.query("INSERT INTO fruits (name) VALUES ('Lychee')").execute() }
            .await().indefinitely()
    }

    fun onStop(@Observes ev: ShutdownEvent?) {

    }

}