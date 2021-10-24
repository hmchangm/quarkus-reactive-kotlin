package tw.idv.brandy.arrow.bean


import java.util.*


data class Fruit(
    val id: String=UUID.randomUUID().toString(),
    val name: String,
    val desc: String
)

data class NewFruit(
    val name: String,
    val desc:String
)
