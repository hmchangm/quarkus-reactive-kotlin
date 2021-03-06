package tw.idv.brandy.arrow

import org.jboss.logging.Logger
import javax.ws.rs.core.Response

sealed class KaqAppError {
    data class DatabaseProblem(val e: Throwable) : KaqAppError()
    data class FileReadError(val e: Throwable) : KaqAppError()
    data class NoThisFruit(val fruitName: String) : KaqAppError()
    data class AddToDBError(val name: String) : KaqAppError()
    class JsonSerializationFail(val e: Throwable) : KaqAppError()

    companion object {
        private val LOG: Logger = Logger.getLogger(KaqAppError::class.java)
        fun toResponse(kaqError: KaqAppError): Response = when (kaqError) {
            is FileReadError -> Response.serverError().entity(kaqError.e.stackTraceToString()).build()
            is JsonSerializationFail -> {
                LOG.error("Json Serialization Failed", kaqError.e)
                Response.serverError().entity("Json Serialization Failed\n ${kaqError.e.stackTraceToString()}")
                    .build()
            }
            is DatabaseProblem -> {
                LOG.error("db error", kaqError.e)
                Response.serverError().entity("Db Connect Error \n ${kaqError.e.stackTraceToString()}")
                    .build()
            }
            is NoThisFruit -> Response.status(404).entity("FruitId ${kaqError.fruitName} is not exist").build()
            is AddToDBError -> Response.serverError().entity("Fruit ${kaqError.name} add to db failed").build()
        }
    }
}