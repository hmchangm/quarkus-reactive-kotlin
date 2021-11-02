package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Fruit(
    val id: Long,
    val name: String

)
