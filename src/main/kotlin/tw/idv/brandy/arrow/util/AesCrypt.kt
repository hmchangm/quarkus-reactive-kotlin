package tw.idv.brandy.arrow.util

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

fun String.aesEncryptGCM(password: String, transformation: String = "AES/GCM/NoPadding"): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val ivParameterSpec = GCMParameterSpec(128, password.take(16).toByteArray())
    val cipher = Cipher.getInstance(transformation)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
    val encryptedValue = cipher.doFinal(this.toByteArray())
    return encryptedValue.let { Base64.getEncoder().encodeToString(it) }
}

/**
 * AES GCM模式解密
 * @param password 密钥 密钥支持 16/32位
 * @param transformation 加密模式
 */
fun String.aesDecryptGCM(password: String, transformation: String = "AES/GCM/NoPadding"): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val ivParameterSpec = GCMParameterSpec(128, password.take(16).toByteArray())
    val cipher = Cipher.getInstance(transformation)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
    val decryptedByteValue = cipher.doFinal(Base64.getDecoder().decode(this))
    return String(decryptedByteValue)
}


fun main() {
    val salt = "t-Drive_Agent_AP"
    val encoded = "IMSD_AP".aesEncryptGCM(salt)
    println(encoded)
    println(encoded.aesDecryptGCM(salt))
    println("GGGGFGdfasfsffssfsfasfdf".aesDecryptGCM(salt))
}
