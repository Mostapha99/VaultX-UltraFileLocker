package com.vaultx.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "vaults")
data class VaultEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val iconPath: String?,
    val createdAt: Date,
    val updatedAt: Date,
    val isHidden: Boolean = false,
    val isFakeVault: Boolean = false,
    val encryptionKeyId: String,
    val fileCount: Int = 0,
    val totalSize: Long = 0L,
    val vaultType: VaultType = VaultType.PERSONAL,
    val customTheme: String? = null
)

enum class VaultType {
    PERSONAL,
    SECRET,
    BUSINESS,
    MEDIA,
    DOCUMENTS,
    CUSTOM
}

