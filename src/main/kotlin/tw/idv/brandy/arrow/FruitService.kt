package tw.idv.brandy.arrow

import arrow.core.Either
import arrow.core.flatMap
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.repo.FruitKRepo
import tw.idv.brandy.arrow.repo.FruitRepo

class FruitService {
    companion object {
        suspend fun findAll(): Either<KaqAppError, List<Fruit>> = FruitKRepo.findAll()
        suspend fun findAllQ(): Either<KaqAppError, List<Fruit>> = FruitRepo.findAll()
        suspend fun create(fruit: Fruit) = FruitKRepo.add(fruit).flatMap { FruitKRepo.findByName(fruit.name) }
        suspend fun findByName(name: String): Either<KaqAppError, Fruit> = FruitKRepo.findByName(name)
    }
}