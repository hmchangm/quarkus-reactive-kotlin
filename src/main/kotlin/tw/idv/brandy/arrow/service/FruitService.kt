package tw.idv.brandy.arrow.service

import arrow.core.Either
import arrow.core.flatMap
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.model.Fruit
import tw.idv.brandy.arrow.repo.FruitRepo

typealias KaqAppErrorFruitListEither = Either<KaqAppError, List<Fruit>>

object FruitService {

    private suspend fun findAllWithTimePrint(
        finder: suspend () -> KaqAppErrorFruitListEither,
        method: String
    ): KaqAppErrorFruitListEither =
        Either.catch {
            val start = System.currentTimeMillis()
            val all = finder()
            val done = System.currentTimeMillis()
            println("$method Load time : ${done - start}")
            return all
        }.mapLeft { KaqAppError.DatabaseProblem(it) }

    suspend fun findAll(): KaqAppErrorFruitListEither = findAllWithTimePrint(FruitRepo.findAll, "Coroutines")
    suspend fun create(fruit: Fruit) = FruitRepo.add(fruit).flatMap { FruitRepo.findByName(fruit.name) }
    suspend fun findByName(name: String): Either<KaqAppError, Fruit> = FruitRepo.findByName(name)

}