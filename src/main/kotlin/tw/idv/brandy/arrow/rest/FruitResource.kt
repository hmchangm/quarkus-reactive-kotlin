package tw.idv.brandy.arrow.rest

import arrow.core.Either
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.bean.Fruit
import tw.idv.brandy.arrow.bean.Greeting
import tw.idv.brandy.arrow.repo.FruitRepo
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/")
class FruitResource {

    @GET
    @Path("/greeting")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello() = Greeting("hello")

    @GET
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruits(): Response =
        FruitRepo.findAll().fold(
            ifRight = { error -> Response.ok(error).build() },
            ifLeft = { fruits -> KaqAppError.toResponse(fruits) }
        )

    @GET
    @Path("/fruits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(id:Long): Response =
        FruitRepo.findById(id).fold(
            ifRight = { error -> Response.ok(error).build() },
            ifLeft = { fruit -> KaqAppError.toResponse(fruit) }
        )


}