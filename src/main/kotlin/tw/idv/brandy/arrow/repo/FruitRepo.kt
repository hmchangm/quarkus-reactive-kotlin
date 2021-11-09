package tw.idv.brandy.arrow.repo

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.toOption
import com.mongodb.client.MongoCollection
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.Document
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.util.DatabaseInit.mongoClient


object FruitRepo {

    val findAll: suspend () -> Either<KaqAppError, List<Fruit>> = {
        Either.catch {
            getCollection().find().map(docToFruit).toList()
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
    }

    val add: suspend (fruit: Fruit) -> Either<KaqAppError, Unit> = { fruit ->
        Either.catch {
            Document()
                .append("name", fruit.name)
                .append("desc", fruit.desc)
                .append("id", fruit.id).let { getCollection().insertOne(it) }
            Unit
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
    }

    private fun getCollection(): MongoCollection<Document> {
        return mongoClient.getDatabase("fruit").getCollection("fruit")
    }

    private val fruitOptionToEither: suspend (maybeFruit: Option<Fruit>, name: String) -> Either<KaqAppError, Fruit> =
        { maybeFruit, name -> maybeFruit.toEither { KaqAppError.NoThisFruit(name) } }

    suspend fun findByName(name: String): Either<KaqAppError, Fruit> = Either.catch {
        BsonDocument().append("name", BsonString(name)).let { getCollection().find(it) }
            .firstNotNullOfOrNull(docToFruit).toOption()
    }.mapLeft { KaqAppError.DatabaseProblem(it) }.flatMap { fruitOptionToEither(it, name) }

    private val docToFruit: (Document) -> Fruit = { doc: Document ->
        Fruit(doc.getString("id"), doc.getString("name"), doc.getOrDefault("desc", "") as String)
    }


}