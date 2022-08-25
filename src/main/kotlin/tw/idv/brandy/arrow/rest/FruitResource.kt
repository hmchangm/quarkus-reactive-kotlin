package tw.idv.brandy.arrow.rest


import com.fasterxml.jackson.databind.ObjectMapper
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

@Path("/")
class FruitResource(val json: ObjectMapper) {

    @GET
    @Path("/greeting")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello() = Greeting("hello")

    @GET
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruits(): RestResponse<String> =
        FruitRepo.findAll().map { json.writeValueAsString(it) }.fold(
            ifRight = { RestResponse.ok(it) },
            ifLeft = KaqAppError::toResponse
        )

    @GET
    @Path("/fruits/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(id: Long): RestResponse<String> =
        FruitRepo.findById(id).map { json.writeValueAsString(it) }.fold(
            ifRight = { RestResponse.ok(it) },
            ifLeft = KaqAppError::toResponse
        )

    @POST
    @Path("/fruits")
    suspend fun create(fruit: Fruit): RestResponse<String> =
        FruitRepo.create(fruit).map { json.writeValueAsString(it) }.fold(
            ifRight = {
                RestResponse.ResponseBuilder.create<String>(201, "")
                    .entity(it).build()
            },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}