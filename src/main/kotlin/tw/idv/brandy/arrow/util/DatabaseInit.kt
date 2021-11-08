package tw.idv.brandy.arrow.util

import com.mongodb.client.MongoClient
import javax.enterprise.inject.spi.CDI

object DatabaseInit {
    val mongoClient: MongoClient by lazy { CDI.current().select(MongoClient::class.java).get() }

}
