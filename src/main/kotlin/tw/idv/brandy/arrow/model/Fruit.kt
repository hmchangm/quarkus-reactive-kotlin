package tw.idv.brandy.arrow.model


import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Fruit (
    val id: String=UUID.randomUUID().toString(),
    val name: String,
    val desc: String
): java.io.Serializable
