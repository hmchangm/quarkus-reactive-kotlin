package tw.idv.brandy.arrow

import javax.ws.rs.core.Response

sealed class KaqAppError {
    data class DatabaseProblem(val e :Throwable): KaqAppError()
    data class FileReadError(val e :Throwable): KaqAppError()
    data class SomeError(val uuid: String): KaqAppError()
    data class NoThisFruit(val fruitId: Long): KaqAppError()

    companion object {
        fun toResponse(kaqError: KaqAppError): Response = when (kaqError) {
            is FileReadError -> Response.serverError().entity(kaqError.e.stackTraceToString()).build()
            is DatabaseProblem -> Response.serverError().entity("Db Connection Error ${kaqError.e.stackTraceToString()}")
                .build()
            is SomeError -> Response.serverError().entity("SomeError").build()
            is NoThisFruit -> Response.serverError().entity("The fruitId ${kaqError.fruitId} is not exist").build()
        }
    }
}