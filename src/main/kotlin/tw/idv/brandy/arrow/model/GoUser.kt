package tw.idv.brandy.arrow.model

import kotlinx.serialization.Serializable

// {"id":3950,"name":"Amb. Dinakar Nambeesan","email":"dinakar_amb_nambeesan@considine.co","gender":"male","status":"inactive"}
@Serializable
data class GoUser (
    val id:Int,
    val name:String,
    val email:String,
    val gender:String,
    val status:String
        )
