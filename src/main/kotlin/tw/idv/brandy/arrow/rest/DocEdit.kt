package tw.idv.brandy.arrow.rest

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.UploadObjectArgs
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
class DocEdit(val minioClient: MinioClient) {

    private val classLoader: ClassLoader = javaClass.classLoader

    @GET
    @Path("/abcd/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    suspend fun downloadFile(fileName: String) = Response.ok(readInputStream(fileName)).build()

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
    val token = UUID.randomUUID().toString()
    @LOCK
    @Path("/abcd/{fileName}")
    suspend fun lockCall(body: String, @PathParam("fileName") fileName: String): Response {

        println(token)
        return Response.ok().apply {
                println("lock call : $body")
                header("Lock-Token", token)
                header("Content-Type", "text/xml")
                status(200)
            }.build()
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
    suspend fun save(fileName: String, inputStream: InputStream): Response {
        writeFile(fileName,inputStream)
        return Response.ok().apply {
            status(200)
        }.build()
    }

    suspend fun readInputStream(fileName: String) =
        minioClient.getObject(
            GetObjectArgs.builder()
                .bucket("buck")
                .`object`(fileName)
                .build()
        )!!

    suspend fun writeFile(filename: String, inputStream: InputStream) = withContext(Dispatchers.IO) {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket("buck").stream(inputStream,-1,1024*1024*1024)
                .`object`(filename)
                .build()
        )
    }

}