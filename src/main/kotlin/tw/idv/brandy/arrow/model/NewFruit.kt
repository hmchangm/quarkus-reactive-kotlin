package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class NewFruit(
    val name: String,
    val desc:String
)