package tw.idv.brandy.arrow

import javax.ws.rs.core.Response

sealed class KaqAppError {
    data class DatabaseProblem(val e :Throwable): KaqAppError()
    data class FileReadError(val e :Throwable): KaqAppError()
        data class NoThisFruit(val fruitId: Long): KaqAppError()
    data class AddToDBError(val name: String) : KaqAppError()

    companion object {
        fun toResponse(kaqError: KaqAppError): Response = when (kaqError) {
            is FileReadError -> Response.serverError().entity(kaqError.e.stackTraceToString()).build()
            is DatabaseProblem -> Response.serverError().entity("Db Connect Error ${kaqError.e.stackTraceToString()}")
                .build()
            is NoThisFruit -> Response.status(404).entity("FruitId ${kaqError.fruitId} is not exist").build()
            is AddToDBError -> Response.serverError().entity("Fruit ${kaqError.name} add to db failed").build()
        }
    }
}