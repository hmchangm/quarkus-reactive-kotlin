package tw.idv.brandy.arrow.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Greeting(@JsonProperty val message: String = "",val body:String)