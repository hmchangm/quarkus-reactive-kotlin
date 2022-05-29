package tw.idv.brandy.arrow.rest

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response



@Path("/view")
class DocView {

    private val classLoader: ClassLoader = javaClass.classLoader

    @GET
    @Path("/abcd/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    suspend fun downloadFile(fileName: String) = Response.ok(classLoader.getResourceAsStream(fileName)).build()

    //@OPTIONS
    @Path("/abcd/")
    suspend fun optionParentCall() = Response.ok().apply {
        println("option parent call ")
        status(200)
        header("DAV", "1, 2")
        header("MS-Author-Via", "DAV")
        header("Allow", "OPTIONS,UNLOCK,LOCK,HEAD,PUT,GET")
    }.build()


   // @OPTIONS
    @Path("/abcd/{fileName}")
    suspend fun optionCall() = Response.ok().apply {
        println("option call ")
        status(200)
        header("DAV", "1, 2")
        header("MS-Author-Via", "DAV")
        header("Allow", "OPTIONS,UNLOCK,LOCK,HEAD,PUT,GET")
    }.build()

    @LOCK
    @Path("/abcd/{fileName}")
    suspend fun lockCall(body:String) = Response.ok().apply {
        println("lock call : $body")
        status(403)
    }.build()
}