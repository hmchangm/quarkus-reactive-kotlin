package tw.idv.brandy.arrow

import arrow.core.Either
import arrow.core.flatMap
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.repo.FruitKRepo
import tw.idv.brandy.arrow.repo.FruitRepo

typealias KaqAppErrorFruitListEither = Either<KaqAppError,List<Fruit>>

class FruitService {
    companion object {

        private suspend fun findAllWithTimePrint(finder :suspend ()-> KaqAppErrorFruitListEither,type:String): KaqAppErrorFruitListEither {
            val start = System.currentTimeMillis()
            val all = finder()
            val done = System.currentTimeMillis()
            println("$type : Load time : ${done-start}")
            return all
        }
//curl -w %{time_connect}:%{time_starttransfer}:%{time_total} -s "http://localhost:8080/fruitq" -o NUL
        suspend fun findAll(): KaqAppErrorFruitListEither = findAllWithTimePrint(FruitKRepo.findAll,"Kmongo")
        suspend fun findAllQ(): KaqAppErrorFruitListEither =findAllWithTimePrint(FruitRepo.findAll,"Quarkus")
        suspend fun create(fruit: Fruit) = FruitKRepo.add(fruit).flatMap { FruitKRepo.findByName(fruit.name) }
        suspend fun findByName(name: String): Either<KaqAppError, Fruit> = FruitKRepo.findByName(name)
    }
}