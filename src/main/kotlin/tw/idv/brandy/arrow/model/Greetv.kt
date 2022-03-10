package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection
import kotlinx.serialization.Serializable
@Serializable
@RegisterForReflection
data class Greetv(
    val message: Message
)

@JvmInline
@Serializable
value class Message(private val s: String)