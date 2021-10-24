package tw.idv.brandy.arrow

import arrow.core.Either
import arrow.core.flatMap
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.repo.FruitRepo

class FruitService {
    companion object {
        suspend fun findAll(): Either<KaqAppError, List<Fruit>> = FruitRepo.findAll()
        suspend fun create(fruit: Fruit) = FruitRepo.add(fruit).flatMap { FruitRepo.findByName(fruit.name) }
        suspend fun findByName(name: String): Either<KaqAppError, Fruit> = FruitRepo.findByName(name)
    }
}