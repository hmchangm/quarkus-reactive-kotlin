package tw.idv.brandy.arrow.rest

import com.github.michaelbull.retry.policy.limitAttempts
import com.github.michaelbull.retry.retry
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.*
import org.jboss.resteasy.reactive.RestResponse
import java.io.File
import java.io.FilterInputStream
import java.io.InputStream
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.HttpHeaders
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

    @GET
    @Path("/getFile/{fileName}")
    //@Produces(MediaType.APPLICATION_OCTET_STREAM)
    suspend fun getFile(fileName: String, rc: RoutingContext): ByteArray {
        //return buffer
        streamInputStream(rc, fileName, readInputStream(fileName))
        return byteArrayOf()
    }

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
    suspend fun unlock(@HeaderParam("Lock-Token") token: String, body: String) =
        RestResponse.ResponseBuilder.ok("").apply {
            println("unlock call : $body")
            println("unlock token : $token")
        }.build()

    @PUT
    @Path("/abcd/{fileName}")
    suspend fun save(fileName: String, inputStream: InputStream) =
        writeFile(fileName, inputStream).let { RestResponse.ResponseBuilder.ok("").build() }

    @POST
    @Path("/abcd/{fileName}")
    suspend fun saveForm(fileName: String, file: File) =
       println(file.absoluteFile).let{ writeFile(fileName, file.inputStream())}.let { RestResponse.ResponseBuilder.ok("").build() }


    suspend fun readInputStream(fileName: String) =
        withContext(Dispatchers.IO) {
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket("buck")
                    .`object`(fileName)
                    .build()
            )!!
        }



    suspend fun writeFile(filename: String, inputStream: InputStream) = retry(limitAttempts(3)) {
        withContext(Dispatchers.IO) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket("buck").stream(inputStream, -1, 1024 * 1024 * 1024)
                    .`object`(filename)
                    .build()
            )
        }
    }


    suspend fun InputStream.bioRead(b: ByteArray) = withContext(Dispatchers.IO) {
        read(b)
    }

    suspend fun streamInputStream(
        rc: RoutingContext,
        fileName: String,
        inputStream: InputStream
    ): ByteArray {
        val resp = rc.response().apply {
            putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
            putHeader(
                "Content-Disposition",
                "attachment; filename=\"$fileName\""
            )
            isChunked = true
        }

        val buffer = ByteArray(8192)
        var c: Int
        while (inputStream.bioRead(buffer).also { c = it } != -1) {
            buffer.let {
                when (c == 8192) {
                    true -> it
                    false -> it.copyOfRange(0, c)
                }
            }.let { Buffer.buffer(it) }.let { resp.write(it).await() }
        }
        withContext(Dispatchers.IO) {
            inputStream.close()
        }
        // let resteasy jackson end the response properly
        return byteArrayOf()
    }

}

