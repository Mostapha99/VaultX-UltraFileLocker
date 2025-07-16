package com.vaultx.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.Date

@Entity(
    tableName = "vault_files",
    foreignKeys = [
        ForeignKey(
            entity = VaultEntity::class,
            parentColumns = ["id"],
            childColumns = ["vaultId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["vaultId"])]
)
data class VaultFileEntity(
    @PrimaryKey
    val id: String,
    val vaultId: String,
    val originalName: String,
    val encryptedFileName: String,
    val filePath: String,
    val mimeType: String,
    val fileSize: Long,
    val createdAt: Date,
    val updatedAt: Date,
    val lastAccessedAt: Date?,
    val thumbnailPath: String?,
    val fileType: FileType,
    val isStarred: Boolean = false,
    val aiTags: List<String> = emptyList(),
    val customTags: List<String> = emptyList(),
    val accessCount: Int = 0,
    val duration: Long? = null, // For media files
    val resolution: String? = null, // For images/videos
    val checksum: String? = null
)

enum class FileType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    APK,
    ARCHIVE,
    OTHER
}

