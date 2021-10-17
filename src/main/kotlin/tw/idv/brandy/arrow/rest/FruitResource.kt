package tw.idv.brandy.arrow.rest


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
            ifRight = { fruits -> Response.ok(fruits).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @GET
    @Path("/fruits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(id:Long): Response =
        FruitRepo.findById(id).fold(
            ifRight = { fruit -> Response.ok(fruit).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @POST
    @Path("/fruits")
    suspend fun create(fruit:Fruit): Response =
        FruitRepo.create(fruit).fold(
            ifRight = {  Response.ok(it).status(201).build()},
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}