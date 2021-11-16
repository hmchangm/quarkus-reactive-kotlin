package tw.idv.brandy.arrow.service

import arrow.core.*
import arrow.core.computations.either
import arrow.fx.coroutines.parZip
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

    suspend fun findAll(): Either<KaqAppError, List<Fruit>> = findAllWithTimePrint(FruitRepo.findAll, "Coroutines")
    suspend fun create(fruit: Fruit) = FruitRepo.add(fruit).flatMap { FruitRepo.findByName(fruit.name) }
    suspend fun findByName(name: String): Either<KaqAppError, Fruit> = FruitRepo.findByName(name)

    suspend fun createAndFind(fruit: Fruit, name: String): Either<KaqAppError, String> = either {
        val newOne = create(fruit).bind()
        val nameFruit = findByName(name).bind()
        val fruits = findAll().bind()
        nameFruit.name
    }

    suspend fun createAndFindPar(fruit: Fruit, name: String): Either<KaqAppError, String> =
        parZip({ create(fruit) }, { findByName(name) }, { findAll() }) { _, nameFruit, fruits ->
            nameFruit.map { it.name }
        }

    suspend fun createAndFindZip(fruit: Fruit, name: String): Either<KaqAppError, String> =
        create(fruit).zip(findByName(name), findAll()) { newOne, nameFruit, _ ->
            nameFruit.name
        }

}