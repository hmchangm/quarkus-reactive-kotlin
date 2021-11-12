package tw.idv.brandy.arrow.rest

import arrow.core.Either
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.*
import tw.idv.brandy.arrow.service.FruitService
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/")
class FruitResource {
    val mapper = jacksonObjectMapper()

    @GET
    @Path("/greeting")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello(): Greeting = Greeting("hello")

    @GET
    @Path("/greetv")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun greetv(): Greetv = Greetv(Message("hello"))


    @POST
    @Path("/greetIncoming")
    suspend fun greetIncoming(s: String): Response {
        return Either.catch {
            val g: Greetv = Json.decodeFromString(s)
            g
        }.mapLeft { KaqAppError.DatabaseProblem(it) }
            .fold(ifRight = {
                Response.ok(
                    Json.encodeToString(it), MediaType.APPLICATION_JSON
                ).build()
            },
                ifLeft = { err -> KaqAppError.toResponse(err) })
    }


    @GET
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruits(): Response =
        FruitService.findAll().fold(
            ifRight = { fruits -> Response.ok(fruits).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @GET
    @Path("/fruits/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(name: String): Response =
        FruitService.findByName(name).fold(
            ifRight = { fruit -> Response.ok(fruit).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @POST
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun create(fruit: NewFruit): Response =
        FruitService.create(Fruit(name = fruit.name, desc = fruit.desc)).fold(
            ifRight = { Response.ok(it).status(201).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}