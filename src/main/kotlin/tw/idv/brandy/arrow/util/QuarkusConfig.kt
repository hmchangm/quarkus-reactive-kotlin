package tw.idv.brandy.arrow.util

import arrow.core.*
import org.eclipse.microprofile.config.ConfigProvider
import tw.idv.brandy.arrow.KaqAppError

object QuarkusConfig {

    private val evalParams: Eval<Either<KaqAppError, Triple<String, String, String>>> = Eval.later {
        fetch<String>("quarkus.package.type").zip(
            fetch<String>("myConfig.xxx"),
            fetch<String>("myConfig.xxx")
        ) { a, b, c -> Triple(a, b, c) }.toEither().mapLeft { KaqAppError.QuarkusConfigError(it) }
    }


    fun getParams() = evalParams.value()

    inline fun <reified T> fetch(key: String): ValidatedNel<ConfigError, T> =
        Validated.fromEither(
            Either.catch {
                ConfigProvider.getConfig().getValue(key, T::class.java)
            }.mapLeft { ConfigError.MissingConfig(key) }
        ).toValidatedNel()

}

sealed class ConfigError {
    data class MissingConfig(val key: String) : ConfigError()

}