package tw.idv.brandy.arrow.model

import kotlinx.serialization.Serializable

@Serializable
data class NewFruit(
    val name: String,
    val desc:String
)