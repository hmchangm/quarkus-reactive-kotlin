package tw.idv.brandy.arrow.bean

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Fruit(
    val id: Long,
    val name: String

)
