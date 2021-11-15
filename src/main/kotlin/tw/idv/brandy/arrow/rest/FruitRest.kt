package tw.idv.brandy.arrow.rest

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.*
import tw.idv.brandy.arrow.repo.FruitRepo
import tw.idv.brandy.arrow.service.FruitService
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/fruits")
class FruitRest {

    @GET
    @Path("/all")
    suspend fun getAll(): Response {
        return FruitRepo.findFruitModels().fold(
            ifRight = {
                Response.ok(
                    Json.encodeToString(it), MediaType.APPLICATION_JSON
                ).build()
            },
            ifLeft = { err -> KaqAppError.toResponse(err) })
    }

    @GET
    @Path("/alls")
    suspend fun getAlls(): Response {
        return FruitRepo.findFruitModels().fold(
            ifRight = {
                Response.ok(
                    it, MediaType.APPLICATION_JSON
                ).build()
            },
            ifLeft = { err -> KaqAppError.toResponse(err) })
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruits(): Response =
        FruitService.findAll().fold(
            ifRight = { fruits -> Response.ok(fruits).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(name: String): Response =
        FruitService.findByName(name).fold(
            ifRight = { fruit -> Response.ok(fruit).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @POST

    @Produces(MediaType.APPLICATION_JSON)
    suspend fun create(fruit: NewFruit): Response =
        FruitService.create(Fruit(name = fruit.name, desc = fruit.desc)).fold(
            ifRight = { Response.ok(it).status(201).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}