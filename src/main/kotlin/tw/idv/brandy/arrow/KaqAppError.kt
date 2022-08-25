package tw.idv.brandy.arrow

import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestResponse


sealed class KaqAppError {
    data class DatabaseProblem(val e: Throwable) : KaqAppError()
    data class FileReadError(val e: Throwable) : KaqAppError()
    data class NoThisFruit(val fruitId: Long) : KaqAppError()
    data class AddToDBError(val name: String) : KaqAppError()

    companion object {
        private val LOG: Logger = Logger.getLogger(KaqAppError::class.java)
        fun toResponse(kaqError: KaqAppError): RestResponse<String> = when (kaqError) {
            is FileReadError -> RestResponse.ResponseBuilder.serverError<String>()
                .entity(kaqError.e.stackTraceToString()).build()
            is DatabaseProblem -> {
                LOG.error("db error", kaqError.e)
                ServerErrorResponse().entity("Db Connect Error ${kaqError.e.stackTraceToString()}")
                    .build()
            }
            is NoThisFruit -> ServerErrorResponse().entity("FruitId ${kaqError.fruitId} is not exist").build()
            is AddToDBError -> ServerErrorResponse().entity("Fruit ${kaqError.name} add to db failed").build()
        }

        private fun ServerErrorResponse() =
            RestResponse.ResponseBuilder.notFound<String>()
    }
}