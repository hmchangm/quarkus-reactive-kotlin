package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.*

@RegisterForReflection
data class Fruit(

    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val desc: String
)
