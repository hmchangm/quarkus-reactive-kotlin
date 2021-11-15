package tw.idv.brandy.arrow.model


import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class FruitId(private val value: String)
@Serializable
@JvmInline
value class FruitName(private val v: String)
@Serializable
@JvmInline
value class FruitDesc(private val v: String)

@Serializable
@RegisterForReflection
data class FruitModel (
    @JsonProperty
    val id: FruitId,
    @JsonProperty
    val name: FruitName,
    @JsonProperty
    val desc: FruitDesc
)
