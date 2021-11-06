package tw.idv.brandy.arrow.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class NewFruit(
    @JsonProperty
    val name: String,
    @JsonProperty
    val desc:String
)