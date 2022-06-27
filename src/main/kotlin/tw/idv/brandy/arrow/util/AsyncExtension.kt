package tw.idv.brandy.arrow.util

import com.mongodb.client.MongoCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.conversions.Bson


suspend fun <T : Any> MongoCollection<T>.asyncFind() = withContext(Dispatchers.IO){
    find()
}

suspend fun <T : Any> MongoCollection<T>.asyncFind(bson: Bson) = withContext(Dispatchers.IO){
    find(bson)
}

suspend fun <T : Any> MongoCollection<T>.asyncInsertOne(t:T) = withContext(Dispatchers.IO){
    insertOne(t)
}