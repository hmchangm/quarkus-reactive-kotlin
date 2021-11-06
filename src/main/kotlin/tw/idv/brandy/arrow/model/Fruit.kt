package tw.idv.brandy.arrow.model


import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.*

@RegisterForReflection
data class Fruit (
    @JsonProperty
    val id: String=UUID.randomUUID().toString(),
    @JsonProperty
    val name: String,
    @JsonProperty
    val desc: String
)
