package tw.idv.brandy.arrow.rest

import arrow.core.Either
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.*
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/greeting")
class GreetingRest {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello(): Greeting = Greeting("hello")

    @GET
    @Path("/value")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun greetValue(): Greetv = Greetv(Message("hello"))


    @POST
    suspend fun greetInOut(s: String): Response {
        return Either.catch {
            val g: Greetv = Json.decodeFromString(s)
            g
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
            .fold(ifRight = {
                Response.ok(Json.encodeToString(it), MediaType.APPLICATION_JSON).build()
            },
                ifLeft = { err -> KaqAppError.toResponse(err) })
    }

}