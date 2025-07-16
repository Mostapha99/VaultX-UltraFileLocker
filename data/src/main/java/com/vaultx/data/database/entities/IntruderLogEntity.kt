package com.vaultx.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "intruder_logs")
data class IntruderLogEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Date,
    val attemptType: AttemptType,
    val enteredValue: String?, // PIN/Pattern that was entered
    val deviceInfo: String?,
    val selfieImagePath: String?,
    val location: String? = null,
    val ipAddress: String? = null,
    val isResolved: Boolean = false
)

enum class AttemptType {
    PIN_FAILED,
    PATTERN_FAILED,
    BIOMETRIC_FAILED,
    MULTIPLE_ATTEMPTS,
    SUSPICIOUS_ACTIVITY
}

