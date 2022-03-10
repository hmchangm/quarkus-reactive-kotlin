package tw.idv.brandy.arrow.rest

import io.smallrye.mutiny.coroutines.awaitSuspending
import tw.idv.brandy.arrow.util.DatabaseInit.Companion.redisClient
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response


@Path("/redis")
class RedisRest {


    @GET
    @Path("add")
    suspend fun add() = UUID.randomUUID().toString().let {
        redisClient.set(
            listOf("key1", it)
        ).awaitSuspending()
    }.let { Response.ok(it).build() }


    @GET
    @Path("get")
    suspend fun get() = UUID.randomUUID().toString().let {
        redisClient.get(
            "key1"
        ).awaitSuspending()
    }.let { Response.ok(it.toString()).build() }


}