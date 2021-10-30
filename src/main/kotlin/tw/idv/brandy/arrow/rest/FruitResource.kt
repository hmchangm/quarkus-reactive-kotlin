package tw.idv.brandy.arrow.rest

import tw.idv.brandy.arrow.FruitService
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.model.Greeting
import tw.idv.brandy.arrow.model.NewFruit
import java.util.concurrent.ThreadLocalRandom
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.streams.asSequence


@Path("/")
class FruitResource {

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun randomString(length:Long):String  {
        return ThreadLocalRandom.current()
        .ints(length, 0, charPool.size)
        .asSequence()
        .map(charPool::get)
        .joinToString("")
    }

    @GET
    @Path("/greeting")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun hello() = Greeting("hello")

    @GET
    @Path("/createFruits")
    suspend fun createFruits(): String = run {
        for (i in 0..50000) {
            FruitService.create(Fruit(name=randomString(100),desc = randomString(400))).fold(
                ifRight = {  Response.ok(it).status(201).build()},
                ifLeft = { err -> KaqAppError.toResponse(err) }
            )

        }

    }.let{"done"}




    @GET
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruits(): Response =
        FruitService.findAll().fold(
            ifRight = { fruits -> Response.ok(fruits).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @GET
    @Path("/fruitq")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllFruitq(): Response =
        FruitService.findAllQ().fold(
            ifRight = { fruits -> Response.ok(fruits).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @GET
    @Path("/fruits/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getSingle(name:String): Response =
        FruitService.findByName(name).fold(
            ifRight = { fruit -> Response.ok(fruit).build() },
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

    @POST
    @Path("/fruits")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun create(fruit: NewFruit): Response =
        FruitService.create(Fruit(name=fruit.name,desc = fruit.desc)).fold(
            ifRight = {  Response.ok(it).status(201).build()},
            ifLeft = { err -> KaqAppError.toResponse(err) }
        )

}