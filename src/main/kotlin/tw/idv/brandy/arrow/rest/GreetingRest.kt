package tw.idv.brandy.arrow.rest

import arrow.core.Either
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Greeting
import tw.idv.brandy.arrow.model.Greetv
import tw.idv.brandy.arrow.model.Message

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response



@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention
@HttpMethod("PROPFIND")
annotation class PROPFIND

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention
@HttpMethod("LOCK")
annotation class LOCK

@Path("/greeting")
class GreetingRest {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello(): Greeting = Greeting("hello")

    @LOCK
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun helloLock(): Greeting = Greeting("hello LOCK")

    @PROPFIND
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun helloProp(): Greeting = Greeting("hello PROPFIND")


    @OPTIONS
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun helloOptions(): Greeting = Greeting("hello OPTIONS")

    @GET
    @Path("/value")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun greetValue(): Greetv = Greetv(Message("hello"))


    @POST
    suspend fun greetInOut(json: String): Response {
        return Either.catch {
            json.let { Json.decodeFromString(Greetv.serializer(), it) }
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
            .fold(ifRight = {
                Response.ok(Json.encodeToString(it), MediaType.APPLICATION_JSON).build()
            },
                ifLeft = { err -> KaqAppError.toResponse(err) })
    }

}