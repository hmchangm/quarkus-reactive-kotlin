package tw.idv.brandy.arrow.service

import arrow.core.Either
import arrow.core.getOrElse
import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.GoUser
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

typealias  EitherKaqGoUser = Either<KaqAppError,List<GoUser>>

object RestClientService {

    private val myUsersCache = Caffeine.newBuilder()
        .expireAfterWrite(
            5, TimeUnit.SECONDS
        )
        .buildAsync<String,EitherKaqGoUser>()

    suspend fun getUsers(key: String):EitherKaqGoUser =
        Either.catch {
            val httpClient: CloseableHttpClient = HttpClients.createDefault()
            val urlOverHttps = "https://gorest.co.in/public/v2/users"
            val getMethod: HttpGet = HttpGet(urlOverHttps)
             httpClient.execute(getMethod).let { EntityUtils.toString(it.entity) }
                .let { Json.decodeFromString(it) as List<GoUser> }
        }.mapLeft { KaqAppError.DatabaseProblem(it) }

    suspend fun getUserWithCache(key:String):EitherKaqGoUser {
        return myUsersCache.getAwait(key) {
            println("called")
            val users = getUsers(key)
            users
        }
    }
}

suspend fun <K, V> AsyncCache<K, V>.getAwait(key: K, loader: suspend (key: K) -> V): V {
    return get(key) { _, executor ->
        val f = CompletableFuture<V>()
        CoroutineScope(executor.asCoroutineDispatcher()).launch {
            f.complete(loader(key))
        }
        f
    }.await()
}

suspend fun <K, V> AsyncCache<K, V>.getAsync(key: K, timeoutMs: Long, mapper: () -> V): V {
    return get(key) { _, executor ->
        val result = CompletableFuture<V>()
        executor.execute {
            try {
                result.complete(mapper())
            } catch (e: Throwable) {
                result.completeExceptionally(e)
            }
        }
        result
    }.get(timeoutMs, TimeUnit.MILLISECONDS)
}