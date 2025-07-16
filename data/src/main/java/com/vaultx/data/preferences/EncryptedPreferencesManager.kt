package com.vaultx.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val PREFS_NAME = "vault_x_encrypted_prefs"
        
        // Keys
        const val KEY_USER_PIN = "user_pin"
        const val KEY_USER_PATTERN = "user_pattern"
        const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        const val KEY_LOCK_DELAY = "lock_delay"
        const val KEY_FIRST_TIME_SETUP = "first_time_setup"
        const val KEY_HIDDEN_MODE_ENABLED = "hidden_mode_enabled"
        const val KEY_FAKE_VAULT_ENABLED = "fake_vault_enabled"
        const val KEY_INTRUDER_DETECTION_ENABLED = "intruder_detection_enabled"
        const val KEY_CURRENT_THEME = "current_theme"
        const val KEY_AUTO_LOCK_ENABLED = "auto_lock_enabled"
        const val KEY_LAST_UNLOCK_TIME = "last_unlock_time"
        const val KEY_APP_LOCKED = "app_locked"
        const val KEY_DATABASE_PASSPHRASE = "database_passphrase"
    }
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    // PIN Management
    fun setUserPin(pin: String) {
        sharedPreferences.edit().putString(KEY_USER_PIN, pin).apply()
    }
    
    fun getUserPin(): String? {
        return sharedPreferences.getString(KEY_USER_PIN, null)
    }
    
    fun clearUserPin() {
        sharedPreferences.edit().remove(KEY_USER_PIN).apply()
    }
    
    // Pattern Management
    fun setUserPattern(pattern: String) {
        sharedPreferences.edit().putString(KEY_USER_PATTERN, pattern).apply()
    }
    
    fun getUserPattern(): String? {
        return sharedPreferences.getString(KEY_USER_PATTERN, null)
    }
    
    fun clearUserPattern() {
        sharedPreferences.edit().remove(KEY_USER_PATTERN).apply()
    }
    
    // Biometric Settings
    fun setBiometricEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
    }
    
    fun isBiometricEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_BIOMETRIC_ENABLED, false)
    }
    
    // Lock Delay Settings
    fun setLockDelay(delayMinutes: Int) {
        sharedPreferences.edit().putInt(KEY_LOCK_DELAY, delayMinutes).apply()
    }
    
    fun getLockDelay(): Int {
        return sharedPreferences.getInt(KEY_LOCK_DELAY, 0) // 0 = immediate
    }
    
    // First Time Setup
    fun setFirstTimeSetup(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_TIME_SETUP, isFirstTime).apply()
    }
    
    fun isFirstTimeSetup(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME_SETUP, true)
    }
    
    // Hidden Mode
    fun setHiddenModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_HIDDEN_MODE_ENABLED, enabled).apply()
    }
    
    fun isHiddenModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_HIDDEN_MODE_ENABLED, false)
    }
    
    // Fake Vault
    fun setFakeVaultEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_FAKE_VAULT_ENABLED, enabled).apply()
    }
    
    fun isFakeVaultEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_FAKE_VAULT_ENABLED, false)
    }
    
    // Intruder Detection
    fun setIntruderDetectionEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_INTRUDER_DETECTION_ENABLED, enabled).apply()
    }
    
    fun isIntruderDetectionEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_INTRUDER_DETECTION_ENABLED, true)
    }
    
    // Theme Settings
    fun setCurrentTheme(theme: String) {
        sharedPreferences.edit().putString(KEY_CURRENT_THEME, theme).apply()
    }
    
    fun getCurrentTheme(): String {
        return sharedPreferences.getString(KEY_CURRENT_THEME, "CYBERPUNK") ?: "CYBERPUNK"
    }
    
    // Auto Lock
    fun setAutoLockEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_AUTO_LOCK_ENABLED, enabled).apply()
    }
    
    fun isAutoLockEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_LOCK_ENABLED, true)
    }
    
    // Last Unlock Time
    fun setLastUnlockTime(timestamp: Long) {
        sharedPreferences.edit().putLong(KEY_LAST_UNLOCK_TIME, timestamp).apply()
    }
    
    fun getLastUnlockTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_UNLOCK_TIME, 0L)
    }
    
    // App Lock Status
    fun setAppLocked(locked: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_APP_LOCKED, locked).apply()
    }
    
    fun isAppLocked(): Boolean {
        return sharedPreferences.getBoolean(KEY_APP_LOCKED, true)
    }
    
    // Database Passphrase
    fun setDatabasePassphrase(passphrase: String) {
        sharedPreferences.edit().putString(KEY_DATABASE_PASSPHRASE, passphrase).apply()
    }
    
    fun getDatabasePassphrase(): String? {
        return sharedPreferences.getString(KEY_DATABASE_PASSPHRASE, null)
    }
    
    // Utility Methods
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
    
    fun hasValidCredentials(): Boolean {
        return getUserPin() != null || getUserPattern() != null
    }
}

