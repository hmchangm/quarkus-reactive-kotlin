package tw.idv.brandy.arrow.rest

import arrow.core.Either
import arrow.core.flatMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.FruitModel
import tw.idv.brandy.arrow.repo.FruitRepo
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/api/fruits")
class ValueFruitRest {

    @GET
    @Path("/kotlinx")
    suspend fun serialAllByKotlinx(): Response {
        return FruitRepo.findFruitModels().flatMap(::kotlinxEncode).fold(
            ifRight = {
                Response.ok(it, MediaType.APPLICATION_JSON).build()
            },
            ifLeft = { err -> KaqAppError.toResponse(err) })
    }

    private fun kotlinxEncode(it: List<FruitModel>) = Either.catch {
        Json.encodeToString(it)
    }.mapLeft { KaqAppError.JsonSerializationFail(it) }

    @GET
    suspend fun serialAllByJackson(): Response {
        return FruitRepo.findFruitModels().fold(
            ifRight = {
                Response.ok(it, MediaType.APPLICATION_JSON).build()
            },
            ifLeft = { err -> KaqAppError.toResponse(err) })
    }


}