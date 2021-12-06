package tw.idv.brandy.arrow.util

import arrow.core.Either
import arrow.core.Eval
import arrow.core.flatMap
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import io.quarkus.arc.Arc
import org.bson.Document
import tw.idv.brandy.arrow.KaqAppError

object MongoConnector {

    private val mongoClient: Eval<Either<KaqAppError, MongoCollection<Document>>> = Eval.later {
        Either.catch {
            Arc.container().instance(MongoClient::class.java).get()
        }.mapLeft {
            KaqAppError.DatabaseProblem(it)
        }.flatMap {
            Either.catch { it.getDatabase("fruit").getCollection("fruit") }.mapLeft {
                KaqAppError.DatabaseProblem(it)
            }
        }
    }

    val acquireMongoClient: () -> Either<KaqAppError, MongoCollection<Document>> = {
        mongoClient.value()
    }


}