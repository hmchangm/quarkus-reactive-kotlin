package tw.idv.brandy.arrow.repo

import arrow.core.*
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.bean.Fruit


class FruitRepo {


    companion object {

        fun from(row: Row): Fruit {
            return Fruit(row.getLong("id"), row.getString("name"))
        }

        suspend fun findById(id: Long): Either<KaqAppError, Fruit> {
            val row = DbConn.dbPool.preparedQuery("SELECT id, name FROM fruits WHERE id = $1")
                .execute(Tuple.of(id))
                .awaitSuspending().firstOrNone()
            return when (row) {
                is Some -> row.value.run(::from).right()
                is None -> KaqAppError.NoThisFruit(id).left()
            }

        }

        val findAll: suspend () -> Either<KaqAppError, List<Fruit>> = {
            Either.catch {
                DbConn.dbPool.query("SELECT id, name FROM fruits ORDER BY name ASC").execute()
                    .awaitSuspending().toList().map { it.run(::from) }
            }.mapLeft { KaqAppError.DatabaseProblem(it) }

        }


    }
}