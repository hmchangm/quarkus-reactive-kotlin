package tw.idv.brandy.arrow.rest

import io.vertx.core.http.HttpServerRequest
import kotlinx.coroutines.*
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.File
import java.io.InputStream
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.Context
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

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention
@HttpMethod("UNLOCK")
annotation class UNLOCK

@Path("/edit")
class DocEdit {

    private val classLoader: ClassLoader = javaClass.classLoader


    @GET
    @Path("/abcd/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    suspend fun downloadFile(fileName: String) = Response.ok(classLoader.getResourceAsStream(fileName)).build()

    @OPTIONS
    @Path("/abcd/")
    suspend fun optionParentCall() = Response.ok().apply {
        println("option parent call ")
        status(200)
        header("DAV", "1, 2")
        header("MS-Author-Via", "DAV")
        header("Allow", "OPTIONS,UNLOCK,LOCK,HEAD,PUT,GET")
    }.build()


    @OPTIONS
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
    suspend fun lockCall(body: String, @PathParam("fileName") fileName: String): Response {
        val token = UUID.randomUUID().toString()
        println(token)
        return """<?xml version="1.0" encoding="UTF-8"?><d:prop xmlns:d="DAV:">            
            <d:lockdiscovery><d:activelock><d:locktype><d:write/></d:locktype><d:lockscope><d:exclusive/>            
            </d:lockscope><d:depth>Infinity</d:depth><d:timeout>Second-604800</d:timeout><d:locktoken>            
            <d:href>$token</d:href></d:locktoken><d:lockroot>            
            <d:href>/edit/abcd/$fileName</d:href></d:lockroot></d:activelock></d:lockdiscovery></d:prop>""".let {
            Response.ok(it).apply {
                println("lock call : $body")
                header("Lock-Token", token)
                header("Content-Type", "text/xml")
                status(200)
            }.build()
        }
    }

    @UNLOCK
    @Path("/abcd/{fileName}")
    suspend fun unlock(@HeaderParam("Lock-Token") token: String, body: String) = Response.ok().apply {
        println("unlock call : $body")
        println("unlock token : $token")
        status(200)
    }.build()

    @PUT
    @Path("/abcd/{fileName}")
    suspend fun save(inputStream: InputStream): Response {
        println(inputStream.readData())
        return Response.ok().apply {
            status(200)
        }.build()
    }


    suspend fun InputStream.readData(): ByteArray = withContext(Dispatchers.IO) {
        while (available() == 0) {
            delay(10)
        }
        val count = available()
        val buffer = ByteArray(count)
        read(buffer, 0, count)
        buffer
    }
}