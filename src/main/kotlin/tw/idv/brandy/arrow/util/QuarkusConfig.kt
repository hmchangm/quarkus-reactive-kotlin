package tw.idv.brandy.arrow.util

import arrow.core.Either
import arrow.core.Validated
import arrow.core.ValidatedNel
import org.eclipse.microprofile.config.ConfigProvider

object QuarkusConfig {

    fun read(key: String): ValidatedNel<ConfigError, String> =
        Validated.fromEither(
            Either.catch {
                ConfigProvider.getConfig().getValue(key, String::class.java)
            }.mapLeft { ConfigError.MissingConfig(key) }
        ).toValidatedNel()

}

sealed class ConfigError {
    data class MissingConfig(val key: String) : ConfigError()

}