package com.vaultx.ultrafilelocker.security

import com.vaultx.core.security.CryptoManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File
import java.io.FileOutputStream

@RunWith(RobolectricTestRunner::class)
class CryptoManagerTest {
    
    private lateinit var cryptoManager: CryptoManager
    private lateinit var testFile: File
    private val testData = "This is test data for encryption and decryption"
    
    @Before
    fun setup() {
        val context = RuntimeEnvironment.getApplication()
        cryptoManager = CryptoManager(context)
        
        // Create test file
        testFile = File(context.filesDir, "test_file.txt")
        FileOutputStream(testFile).use { output ->
            output.write(testData.toByteArray())
        }
    }
    
    @Test
    fun testGenerateKey() {
        val key = cryptoManager.generateKey()
        assertNotNull("Generated key should not be null", key)
        assertEquals("Key should be 32 bytes (256 bits)", 32, key.size)
    }
    
    @Test
    fun testEncryptDecryptData() {
        val key = cryptoManager.generateKey()
        val originalData = testData.toByteArray()
        
        // Encrypt data
        val encryptedData = cryptoManager.encryptData(originalData, key)
        assertNotNull("Encrypted data should not be null", encryptedData)
        assertFalse("Encrypted data should be different from original", 
                   originalData.contentEquals(encryptedData))
        
        // Decrypt data
        val decryptedData = cryptoManager.decryptData(encryptedData, key)
        assertNotNull("Decrypted data should not be null", decryptedData)
        assertArrayEquals("Decrypted data should match original", 
                         originalData, decryptedData)
    }
    
    @Test
    fun testEncryptDecryptFile() {
        val key = cryptoManager.generateKey()
        val encryptedFile = File(testFile.parent, "encrypted_test_file.enc")
        val decryptedFile = File(testFile.parent, "decrypted_test_file.txt")
        
        try {
            // Encrypt file
            val success = cryptoManager.encryptFile(testFile.absolutePath, 
                                                   encryptedFile.absolutePath, key)
            assertTrue("File encryption should succeed", success)
            assertTrue("Encrypted file should exist", encryptedFile.exists())
            assertTrue("Encrypted file should have content", encryptedFile.length() > 0)
            
            // Decrypt file
            val decryptSuccess = cryptoManager.decryptFile(encryptedFile.absolutePath,
                                                          decryptedFile.absolutePath, key)
            assertTrue("File decryption should succeed", decryptSuccess)
            assertTrue("Decrypted file should exist", decryptedFile.exists())
            
            // Verify content
            val originalContent = testFile.readText()
            val decryptedContent = decryptedFile.readText()
            assertEquals("Decrypted content should match original", 
                        originalContent, decryptedContent)
        } finally {
            // Cleanup
            encryptedFile.delete()
            decryptedFile.delete()
        }
    }
    
    @Test
    fun testHashPassword() {
        val password = "testPassword123"
        val salt = "testSalt"
        
        val hash1 = cryptoManager.hashPassword(password, salt)
        val hash2 = cryptoManager.hashPassword(password, salt)
        
        assertNotNull("Hash should not be null", hash1)
        assertEquals("Same password and salt should produce same hash", hash1, hash2)
        
        val differentSaltHash = cryptoManager.hashPassword(password, "differentSalt")
        assertNotEquals("Different salt should produce different hash", 
                       hash1, differentSaltHash)
    }
    
    @Test
    fun testGenerateChecksum() {
        val checksum1 = cryptoManager.generateChecksum(testFile.absolutePath)
        val checksum2 = cryptoManager.generateChecksum(testFile.absolutePath)
        
        assertNotNull("Checksum should not be null", checksum1)
        assertEquals("Same file should produce same checksum", checksum1, checksum2)
        
        // Modify file and check different checksum
        FileOutputStream(testFile, true).use { output ->
            output.write("additional data".toByteArray())
        }
        
        val modifiedChecksum = cryptoManager.generateChecksum(testFile.absolutePath)
        assertNotEquals("Modified file should have different checksum", 
                       checksum1, modifiedChecksum)
    }
    
    @Test
    fun testEncryptionWithWrongKey() {
        val key1 = cryptoManager.generateKey()
        val key2 = cryptoManager.generateKey()
        val originalData = testData.toByteArray()
        
        // Encrypt with key1
        val encryptedData = cryptoManager.encryptData(originalData, key1)
        
        // Try to decrypt with key2 (wrong key)
        try {
            val decryptedData = cryptoManager.decryptData(encryptedData, key2)
            // If no exception is thrown, the decrypted data should not match original
            assertFalse("Decryption with wrong key should not produce original data",
                       originalData.contentEquals(decryptedData))
        } catch (e: Exception) {
            // Exception is expected when using wrong key
            assertTrue("Exception should be thrown for wrong key", true)
        }
    }
    
    @Test
    fun testLargeFileEncryption() {
        // Create a larger test file
        val largeTestFile = File(testFile.parent, "large_test_file.txt")
        val largeData = "Large test data ".repeat(10000) // ~150KB
        
        FileOutputStream(largeTestFile).use { output ->
            output.write(largeData.toByteArray())
        }
        
        val key = cryptoManager.generateKey()
        val encryptedFile = File(testFile.parent, "large_encrypted_file.enc")
        val decryptedFile = File(testFile.parent, "large_decrypted_file.txt")
        
        try {
            // Test encryption and decryption of large file
            val encryptSuccess = cryptoManager.encryptFile(largeTestFile.absolutePath,
                                                          encryptedFile.absolutePath, key)
            assertTrue("Large file encryption should succeed", encryptSuccess)
            
            val decryptSuccess = cryptoManager.decryptFile(encryptedFile.absolutePath,
                                                          decryptedFile.absolutePath, key)
            assertTrue("Large file decryption should succeed", decryptSuccess)
            
            // Verify content
            val originalContent = largeTestFile.readText()
            val decryptedContent = decryptedFile.readText()
            assertEquals("Large file content should match after encryption/decryption",
                        originalContent, decryptedContent)
        } finally {
            // Cleanup
            largeTestFile.delete()
            encryptedFile.delete()
            decryptedFile.delete()
        }
    }
}

