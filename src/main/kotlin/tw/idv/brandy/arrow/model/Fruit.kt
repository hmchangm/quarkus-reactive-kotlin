package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection
import kotlinx.serialization.Serializable

@RegisterForReflection
@Serializable
data class Fruit(
    val id: Long = -1,
    val name: String

)

@Serializable
data class FruitDto(
    val name: String

)
