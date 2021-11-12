package tw.idv.brandy.arrow.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import kotlinx.serialization.Serializable

@Serializable
@RegisterForReflection
data class Greetv(
    @get:JsonProperty("message")
    @param:JsonProperty("message")
    val message: Message
)

@JvmInline
@Serializable
value class Message(private val s: String)