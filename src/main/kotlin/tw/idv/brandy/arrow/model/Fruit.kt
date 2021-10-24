package tw.idv.brandy.arrow.model


import java.util.*


data class Fruit(
    val id: String=UUID.randomUUID().toString(),
    val name: String,
    val desc: String
)
