package com.vaultx.features.security.domain

import com.vaultx.core.security.CryptoManager
import com.vaultx.core.security.IntruderDetector
import com.vaultx.data.database.dao.IntruderLogDao
import com.vaultx.data.database.entities.AttemptType
import com.vaultx.data.database.entities.IntruderLogEntity
import com.vaultx.data.preferences.EncryptedPreferencesManager
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityRepository @Inject constructor(
    private val cryptoManager: CryptoManager,
    private val intruderDetector: IntruderDetector,
    private val preferencesManager: EncryptedPreferencesManager,
    private val intruderLogDao: IntruderLogDao
) {
    
    // PIN Management
    fun setUserPin(pin: String) {
        val hashedPin = hashPin(pin)
        preferencesManager.setUserPin(hashedPin)
        preferencesManager.setFirstTimeSetup(false)
    }
    
    fun validatePin(pin: String): Boolean {
        val storedPin = preferencesManager.getUserPin()
        val hashedPin = hashPin(pin)
        return storedPin == hashedPin
    }
    
    private fun hashPin(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(pin.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    
    // Pattern Management
    fun setUserPattern(pattern: String) {
        val hashedPattern = hashPin(pattern) // Same hashing for pattern
        preferencesManager.setUserPattern(hashedPattern)
        preferencesManager.setFirstTimeSetup(false)
    }
    
    fun validatePattern(pattern: String): Boolean {
        val storedPattern = preferencesManager.getUserPattern()
        val hashedPattern = hashPin(pattern)
        return storedPattern == hashedPattern
    }
    
    // Biometric Settings
    fun setBiometricEnabled(enabled: Boolean) {
        preferencesManager.setBiometricEnabled(enabled)
    }
    
    fun isBiometricEnabled(): Boolean {
        return preferencesManager.isBiometricEnabled()
    }
    
    // Lock Settings
    fun setLockDelay(delayMinutes: Int) {
        preferencesManager.setLockDelay(delayMinutes)
    }
    
    fun getLockDelay(): Int {
        return preferencesManager.getLockDelay()
    }
    
    fun setAutoLockEnabled(enabled: Boolean) {
        preferencesManager.setAutoLockEnabled(enabled)
    }
    
    fun isAutoLockEnabled(): Boolean {
        return preferencesManager.isAutoLockEnabled()
    }
    
    // App Lock Status
    fun lockApp() {
        preferencesManager.setAppLocked(true)
    }
    
    fun unlockApp() {
        preferencesManager.setAppLocked(false)
        preferencesManager.setLastUnlockTime(System.currentTimeMillis())
        intruderDetector.resetFailedAttempts()
    }
    
    fun isAppLocked(): Boolean {
        if (!preferencesManager.isAutoLockEnabled()) {
            return false
        }
        
        val lastUnlockTime = preferencesManager.getLastUnlockTime()
        val lockDelayMs = preferencesManager.getLockDelay() * 60 * 1000L
        val currentTime = System.currentTimeMillis()
        
        return if (lockDelayMs == 0L) {
            // Immediate lock
            preferencesManager.isAppLocked()
        } else {
            // Time-based lock
            currentTime - lastUnlockTime > lockDelayMs
        }
    }
    
    // Security Features
    fun setHiddenModeEnabled(enabled: Boolean) {
        preferencesManager.setHiddenModeEnabled(enabled)
    }
    
    fun isHiddenModeEnabled(): Boolean {
        return preferencesManager.isHiddenModeEnabled()
    }
    
    fun setFakeVaultEnabled(enabled: Boolean) {
        preferencesManager.setFakeVaultEnabled(enabled)
    }
    
    fun isFakeVaultEnabled(): Boolean {
        return preferencesManager.isFakeVaultEnabled()
    }
    
    fun setIntruderDetectionEnabled(enabled: Boolean) {
        preferencesManager.setIntruderDetectionEnabled(enabled)
    }
    
    fun isIntruderDetectionEnabled(): Boolean {
        return preferencesManager.isIntruderDetectionEnabled()
    }
    
    // First Time Setup
    fun isFirstTimeSetup(): Boolean {
        return preferencesManager.isFirstTimeSetup()
    }
    
    fun hasValidCredentials(): Boolean {
        return preferencesManager.hasValidCredentials()
    }
    
    // Intruder Detection
    suspend fun recordFailedAttempt(
        attemptType: AttemptType,
        enteredValue: String? = null
    ) {
        if (preferencesManager.isIntruderDetectionEnabled()) {
            // This will be handled by IntruderDetector
            // The actual implementation would need lifecycle owner for camera
        }
    }
    
    suspend fun logIntruderAttempt(log: IntruderLogEntity) {
        intruderLogDao.insertLog(log)
    }
    
    fun getIntruderLogs(): Flow<List<IntruderLogEntity>> {
        return intruderLogDao.getAllIntruderLogs()
    }
    
    fun getUnresolvedIntruderLogs(): Flow<List<IntruderLogEntity>> {
        return intruderLogDao.getUnresolvedLogs()
    }
    
    suspend fun markIntruderLogAsResolved(logId: String) {
        intruderLogDao.markAsResolved(logId)
    }
    
    suspend fun getUnresolvedIntruderCount(): Int {
        return intruderLogDao.getUnresolvedCount()
    }
    
    // Theme Management
    fun setCurrentTheme(theme: String) {
        preferencesManager.setCurrentTheme(theme)
    }
    
    fun getCurrentTheme(): String {
        return preferencesManager.getCurrentTheme()
    }
    
    // Database Security
    fun getDatabasePassphrase(): String {
        var passphrase = preferencesManager.getDatabasePassphrase()
        if (passphrase == null) {
            // Generate a new passphrase
            passphrase = generateSecurePassphrase()
            preferencesManager.setDatabasePassphrase(passphrase)
        }
        return passphrase
    }
    
    private fun generateSecurePassphrase(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
        return (1..32)
            .map { charset.random() }
            .joinToString("")
    }
    
    // Cleanup
    fun clearAllSecurityData() {
        preferencesManager.clearAllData()
    }
}

