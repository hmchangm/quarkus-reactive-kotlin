package tw.idv.brandy.arrow.repo

import arrow.core.*
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.bson.BsonDocument
import org.bson.BsonString
import org.bson.Document
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.util.DbConn.Companion.dbPool


class FruitRepo {

    companion object {

        suspend fun findAll(): Either<KaqAppError, List<Fruit>> = Either.catch {
            getCollection().find()
                .map(docToFruit).collect().asList().awaitSuspending()
        }.mapLeft { KaqAppError.DatabaseProblem(it) }

        suspend fun add(fruit: Fruit): Either<KaqAppError, Unit> = Either.catch {
            val document = Document()
                .append("name", fruit.name)
                .append("desc", fruit.desc)
                .append("id", fruit.id)
            getCollection().insertOne(document).awaitSuspending()
            Unit
        }.mapLeft { KaqAppError.DatabaseProblem(it) }

        private fun getCollection(): ReactiveMongoCollection<Document> {
            return dbPool.getDatabase("fruit").getCollection("fruit")
        }



        suspend fun findByName(name: String): Either<KaqAppError, Fruit> = Either.catch {
            val document = BsonDocument().append("name", BsonString(name))
            val fruit = getCollection().find(document)
                .map(docToFruit).collect().first().awaitSuspending()
            when (fruit.toOption()) {
                is Some -> return@catch fruit
                is None -> return KaqAppError.NoThisFruit(name).left()
            }

        }.mapLeft { KaqAppError.DatabaseProblem(it) }

        private val docToFruit: (Document) -> Fruit = { doc: Document ->
            val fruit = Fruit(doc.getString("id"), doc.getString("name"),doc.getOrDefault("desc","") as String)
            fruit
        }
    }


}