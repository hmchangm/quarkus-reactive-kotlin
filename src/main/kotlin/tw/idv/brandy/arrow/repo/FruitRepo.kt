package tw.idv.brandy.arrow.repo

import arrow.core.*
import arrow.core.continuations.either
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.sqlQuery
import com.vladsch.kotlin.jdbc.usingDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit


object FruitRepo {


    private val from: (Row) -> Fruit = { row ->
        Fruit(
            row.long("id"),
            row.string("name")
        )
    }

    suspend fun findById(id: Long): Either<KaqAppError, Fruit> = eitherDsAccess { session ->
        sqlQuery("SELECT id, name FROM fruits WHERE id = ?", id)
            .let {
                session.first(it, from)
            }
    }.flatMap { it.toOption().toEither { KaqAppError.NoThisFruit(id) } }

    val findAll: suspend () -> Either<KaqAppError.DatabaseProblem, List<Fruit>> = {
        Either.catch {
            asyncAccess { session ->
                sqlQuery("SELECT id, name FROM fruits ORDER BY name ASC")
                    .let {
                        session.list(it, from)
                    }
            }
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
    }

    private suspend fun add(name: String): Either<KaqAppError, Long> = Either.catch {
        asyncAccess { session ->
            val insertQuery: String = "INSERT INTO fruits (name) VALUES (?)"
            session.updateGetId(sqlQuery(insertQuery, name))
        }
    }.mapLeft { KaqAppError.DatabaseProblem(it) }
        .flatMap { it.toOption().toEither { KaqAppError.NoThisFruit(-1) } }
        .map { it.toLong() }

    // https://arrow-kt.io/docs/patterns/error_handling/
    suspend fun create(fruit: Fruit): Either<KaqAppError, Fruit> =
        either {
            val id = add(fruit.name).bind()
            findById(id).bind()
        }

    suspend fun createFp(fruit: Fruit): Either<KaqAppError, Fruit> =
        add(fruit.name).flatMap { findById(it) }


    suspend fun <T> asyncAccess(consumer: (com.vladsch.kotlin.jdbc.Session) -> T) = withContext(Dispatchers.IO) {
        usingDefault(consumer)
    }

    suspend fun <T> eitherDsAccess(consumer: (com.vladsch.kotlin.jdbc.Session) -> T) = Either.catch {
        asyncAccess(consumer)
    }.mapLeft { KaqAppError.DatabaseProblem(it) }

}