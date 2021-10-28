package tw.idv.brandy.arrow.repo

import arrow.core.*
import arrow.core.computations.either
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.mysqlclient.MySQLClient
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.bean.Fruit
import tw.idv.brandy.arrow.repo.DbConn.Companion.dbPool


class FruitRepo {


    companion object {

        private fun from(row: Row): Fruit {
            return Fruit(row.getLong("id"), row.getString("name"))
        }

        suspend fun findById(id: Long): Either<KaqAppError, Fruit> {
            val row = dbPool.preparedQuery("SELECT id, name FROM fruits WHERE id = ?")
                .execute(Tuple.of(id))
                .awaitSuspending().firstOrNone()
            return when (row) {
                is Some -> row.value.run(::from).right()
                is None -> KaqAppError.NoThisFruit(id).left()
            }

        }

        val findAll: suspend () -> Either<KaqAppError, List<Fruit>> = {
            Either.catch {
                dbPool.query("SELECT id, name FROM fruits ORDER BY name ASC").execute()
                    .awaitSuspending().toList().map { it.run(::from) }
            }.mapLeft { KaqAppError.DatabaseProblem(it) }

        }

        private suspend fun add(name: String): Either<KaqAppError, Long> = Either.catch {
            dbPool.preparedQuery("INSERT INTO fruits (name) VALUES (?)")
                .execute(Tuple.of(name))
                .awaitSuspending().let { it.property(MySQLClient.LAST_INSERTED_ID) }
        }.mapLeft { KaqAppError.DatabaseProblem(it) }

        // https://arrow-kt.io/docs/patterns/error_handling/
        suspend fun create(fruit: Fruit): Either<KaqAppError, Fruit> =
            either {
                val id = add(fruit.name).bind()
                findById(id).bind()
            }

        suspend fun createFp(fruit: Fruit): Either<KaqAppError, Fruit> =
            add(fruit.name).flatMap { findById(it) }
    }


}