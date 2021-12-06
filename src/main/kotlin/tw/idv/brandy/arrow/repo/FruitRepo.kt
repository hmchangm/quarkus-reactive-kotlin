package tw.idv.brandy.arrow.repo

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.toOption
import com.mongodb.client.result.InsertOneResult
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.Document
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.*
import tw.idv.brandy.arrow.util.MongoConnector.acquireMongoClient


object FruitRepo {

    val findAll: suspend () -> Either<KaqAppError, List<Fruit>> = {
        acquireMongoClient().flatMap { client ->
            Either.catch {
                client.find().map(docToFruit).toList()
            }.mapLeft { KaqAppError.DatabaseProblem(it) }
        }
    }

    val findFruitModels: suspend () -> Either<KaqAppError, List<FruitModel>> = {
        acquireMongoClient().flatMap { client ->
            Either.catch {
                client.find().map(docToValueFruit).toList()
            }.mapLeft { KaqAppError.DatabaseProblem(it) }
        }
    }

    val add: (Fruit) -> Either<KaqAppError, InsertOneResult> = { fruit: Fruit ->
        val doc = Document().apply {
            append("name", fruit.name)
            append("desc", fruit.desc)
            append("id", fruit.id)
        }
        acquireMongoClient().flatMap { client ->
            Either.catch {
                client.insertOne(doc)
            }.mapLeft { KaqAppError.DatabaseProblem(it) }
        }
    }


    private val fruitOptionToEither: suspend (maybeFruit: Option<Fruit>, name: String) -> Either<KaqAppError, Fruit> =
        { maybeFruit, name -> maybeFruit.toEither { KaqAppError.NoThisFruit(name) } }

    suspend fun findByName(name: String): Either<KaqAppError, Fruit> {
        return acquireMongoClient().flatMap { client ->
            Either.catch {
                BsonDocument().append("name", BsonString(name)).let { client.find(it) }
                    .firstNotNullOfOrNull(docToFruit).toOption()
            }.mapLeft { KaqAppError.DatabaseProblem(it) }
        }.flatMap { fruitOptionToEither(it, name) }
    }


    private val docToFruit: (Document) -> Fruit = { doc: Document ->
        Fruit(doc.getString("id"), doc.getString("name"), doc.getOrDefault("desc", "") as String)
    }

    private val docToValueFruit: (Document) -> FruitModel = { doc: Document ->
        FruitModel(
            FruitId(doc.getString("id")),
            FruitName(doc.getString("name")),
            FruitDesc(doc.getOrDefault("desc", "") as String)
        )
    }

}