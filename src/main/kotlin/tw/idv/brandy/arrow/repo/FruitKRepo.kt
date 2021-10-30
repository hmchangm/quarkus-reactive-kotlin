package tw.idv.brandy.arrow.repo

import arrow.core.*
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.Document
import org.litote.kmongo.eq
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.util.DbConn.Companion.dbPool
import tw.idv.brandy.arrow.util.KmongoResource


class FruitKRepo {

    companion object {

        private val fruitCollection = KmongoResource.fruitStore.getCollection<Fruit>()
        val findAll: suspend () -> Either<KaqAppError, List<Fruit>> = {
            Either.catch {
                fruitCollection.find().toList()
            }.mapLeft { KaqAppError.DatabaseProblem(it) }
        }

        suspend fun add(fruit: Fruit): Either<KaqAppError, Unit> = Either.catch {
            fruitCollection.insertOne(fruit)
            Unit
        }.mapLeft { KaqAppError.DatabaseProblem(it) }


        suspend fun findByName(name: String): Either<KaqAppError, Fruit> = Either.catch {
            val fruit : Fruit? = fruitCollection.findOne(Fruit::name eq name)
            when (fruit.toOption()) {
                is Some -> return@catch fruit!!
                is None -> return KaqAppError.NoThisFruit(name).left()
            }

        }.mapLeft { KaqAppError.DatabaseProblem(it) }

    }


}