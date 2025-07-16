package com.vaultx.core.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoManager @Inject constructor() {
    
    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "VaultXMasterKey"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
    }
    
    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }
    
    init {
        generateMasterKey()
    }
    
    private fun generateMasterKey() {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }
    
    private fun getMasterKey(): SecretKey {
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
    
    fun encryptData(data: ByteArray): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getMasterKey())
        
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data)
        
        return EncryptedData(encryptedData, iv)
    }
    
    fun decryptData(encryptedData: EncryptedData): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, encryptedData.iv)
        cipher.init(Cipher.DECRYPT_MODE, getMasterKey(), spec)
        
        return cipher.doFinal(encryptedData.data)
    }
    
    fun encryptFile(inputStream: InputStream, outputStream: OutputStream): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getMasterKey())
        
        val iv = cipher.iv
        outputStream.write(iv)
        
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        inputStream.copyTo(cipherOutputStream)
        cipherOutputStream.close()
        
        return iv
    }
    
    fun decryptFile(inputStream: InputStream, outputStream: OutputStream) {
        val iv = ByteArray(GCM_IV_LENGTH)
        inputStream.read(iv)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.DECRYPT_MODE, getMasterKey(), spec)
        
        val cipherInputStream = CipherInputStream(inputStream, cipher)
        cipherInputStream.copyTo(outputStream)
        cipherInputStream.close()
    }
    
    fun generateFileKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }
    
    fun encryptFileKey(fileKey: SecretKey): EncryptedData {
        return encryptData(fileKey.encoded)
    }
    
    fun decryptFileKey(encryptedKey: EncryptedData): SecretKey {
        val keyBytes = decryptData(encryptedKey)
        return SecretKeySpec(keyBytes, KeyProperties.KEY_ALGORITHM_AES)
    }
    
    fun generateSalt(): ByteArray {
        val salt = ByteArray(32)
        java.security.SecureRandom().nextBytes(salt)
        return salt
    }
}

data class EncryptedData(
    val data: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedData

        if (!data.contentEquals(other.data)) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}

