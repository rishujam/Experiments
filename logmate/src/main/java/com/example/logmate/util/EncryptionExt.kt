package com.example.logmate.util


import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/*
 * Created by Sudhanshu Kumar on 31/05/23.
 */

object EncryptionExt {

    /**
     * Encrypts the log messages
     */
    internal fun String.encrypt(keyAlias: String): String {
        val key = getOrCreateKey(keyAlias)
        val cipher = Cipher.getInstance(Constants.TRANSFORMATION)
        key?.let {
            cipher.init(Cipher.ENCRYPT_MODE, it)
        }
        val inputBytes = this.toByteArray(Charsets.UTF_8)
        val encryptedBytes = cipher?.doFinal(inputBytes)
        if (encryptedBytes != null) {
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        }
        return this
    }

    private fun getOrCreateKey(alias: String): SecretKey? {
        val keyStore = KeyStore.getInstance(Constants.ANDROID_KEYSTORE)
        keyStore.load(null)
        val key = keyStore.getKey(alias, null) as SecretKey?
        return key ?: run {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, Constants.ANDROID_KEYSTORE
            )
            val keySpec = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }

}