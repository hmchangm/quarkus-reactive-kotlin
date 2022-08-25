package tw.idv.brandy.arrow.rest


import arrow.core.identity
import org.jboss.resteasy.reactive.RestResponse
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.model.Greeting
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
    suspend fun getAllFruits() =
        FruitRepo.findAll().fold(
            ifRight = ::identity,
            ifLeft = KaqAppError::toResponse
        )

    @GET
    @Path("/fruits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(id: Long) =
        FruitRepo.findById(id).fold(
            ifRight = ::identity,
            ifLeft = KaqAppError::toResponse
        )

    @POST
    @Path("/fruits")
    suspend fun create(fruit: Fruit) =
        FruitRepo.create(fruit).fold(
            ifRight = { RestResponse.ResponseBuilder.create<Fruit>(201, "").entity(it).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}