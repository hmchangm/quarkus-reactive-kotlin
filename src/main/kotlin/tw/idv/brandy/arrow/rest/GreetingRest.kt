package tw.idv.brandy.arrow.rest

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

@Path("/storage")
class GreetingRest {

    private val classLoader: ClassLoader = javaClass.classLoader

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    suspend fun downloadFile(fileName: String) = Response.ok(classLoader.getResourceAsStream(fileName)).build()

//    @HEAD
//    @Path("{fileName}")
//    suspend fun headCall() = Response.ok().build()


    @OPTIONS
    @Path("{fileName}")
    suspend fun optionCall() = Response.ok().apply {
        status(200)
        header("DAV", "1, 2")
        header("MS-Author-Via", "DAV")
        header("Allow", "OPTIONS,UNLOCK,LOCK,HEAD,PUT,GET")
    }.build()

}