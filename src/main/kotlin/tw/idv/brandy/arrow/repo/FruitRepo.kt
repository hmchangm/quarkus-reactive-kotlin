package tw.idv.brandy.arrow.repo

import arrow.core.*
import arrow.core.continuations.either
import com.vladsch.kotlin.jdbc.Row
import com.vladsch.kotlin.jdbc.Session
import com.vladsch.kotlin.jdbc.sqlQuery
import com.vladsch.kotlin.jdbc.usingDefault
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.model.FruitDto


object FruitRepo {


    private val from: (Row) -> Fruit = { row ->
        Fruit(
            row.long("id"),
            row.string("name")
        )
    }

    suspend fun findById(id: Long): Either<KaqAppError, Fruit> = eitherDbAccess { session ->
        sqlQuery("SELECT id, name FROM fruits WHERE id = ?", id)
            .let {
                session.first(it, from)
            }
    }.flatMap { it.toOption().toEither { KaqAppError.NoThisFruit(id) } }

    val findAll: suspend () -> Either<KaqAppError.DatabaseProblem, List<Fruit>> = {
        eitherDbAccess { session ->
            sqlQuery("SELECT id, name FROM fruits ORDER BY name ASC")
                .let {
                    session.list(it, from)
                }
        }
    }

    private suspend fun add(name: String): Either<KaqAppError, Long> = eitherDbAccess { session ->
        val insertQuery: String = "INSERT INTO fruits (name) VALUES (?)"
        session.updateGetId(sqlQuery(insertQuery, name))
    }.map { it!!.toLong() }

    suspend fun create(fruit: FruitDto): Either<KaqAppError, Fruit> =
        either {
            val id = add(fruit.name).bind()
            findById(id).bind()
        }

    suspend fun createFp(fruit: FruitDto): Either<KaqAppError, Fruit> =
        add(fruit.name).flatMap { findById(it) }

    suspend inline fun <T> eitherDbAccess(noinline consumer: (Session) -> T) = usingDefault(consumer)
        .let { withContext(Dispatchers.IO) { it } }
        .let { Either.catch { it } }.mapLeft { KaqAppError.DatabaseProblem(it) }

}