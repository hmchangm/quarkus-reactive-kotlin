package tw.idv.brandy.arrow.model

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Greeting(val message: String)