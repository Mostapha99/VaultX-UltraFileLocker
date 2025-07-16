package com.vaultx.data.database.dao

import androidx.room.*
import com.vaultx.data.database.entities.VaultFileEntity
import com.vaultx.data.database.entities.FileType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface VaultFileDao {
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId ORDER BY createdAt DESC")
    fun getFilesByVault(vaultId: String): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId AND fileType = :fileType ORDER BY createdAt DESC")
    fun getFilesByVaultAndType(vaultId: String, fileType: FileType): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId ORDER BY fileSize DESC")
    fun getFilesByVaultSortedBySize(vaultId: String): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId ORDER BY originalName ASC")
    fun getFilesByVaultSortedByName(vaultId: String): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId AND isStarred = 1 ORDER BY updatedAt DESC")
    fun getStarredFilesByVault(vaultId: String): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE id = :fileId")
    suspend fun getFileById(fileId: String): VaultFileEntity?
    
    @Query("SELECT * FROM vault_files WHERE vaultId = :vaultId AND originalName LIKE '%' || :searchQuery || '%' ORDER BY createdAt DESC")
    fun searchFilesInVault(vaultId: String, searchQuery: String): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE fileType = :fileType ORDER BY createdAt DESC")
    fun getFilesByType(fileType: FileType): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files ORDER BY accessCount DESC LIMIT :limit")
    fun getMostAccessedFiles(limit: Int): Flow<List<VaultFileEntity>>
    
    @Query("SELECT * FROM vault_files WHERE lastAccessedAt >= :since ORDER BY lastAccessedAt DESC")
    fun getRecentlyAccessedFiles(since: Date): Flow<List<VaultFileEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: VaultFileEntity)
    
    @Update
    suspend fun updateFile(file: VaultFileEntity)
    
    @Delete
    suspend fun deleteFile(file: VaultFileEntity)
    
    @Query("DELETE FROM vault_files WHERE id = :fileId")
    suspend fun deleteFileById(fileId: String)
    
    @Query("DELETE FROM vault_files WHERE vaultId = :vaultId")
    suspend fun deleteAllFilesInVault(vaultId: String)
    
    @Query("UPDATE vault_files SET accessCount = accessCount + 1, lastAccessedAt = :accessTime WHERE id = :fileId")
    suspend fun incrementAccessCount(fileId: String, accessTime: Date)
    
    @Query("UPDATE vault_files SET isStarred = :isStarred WHERE id = :fileId")
    suspend fun updateStarredStatus(fileId: String, isStarred: Boolean)
    
    @Query("SELECT COUNT(*) FROM vault_files WHERE vaultId = :vaultId")
    suspend fun getFileCountInVault(vaultId: String): Int
    
    @Query("SELECT SUM(fileSize) FROM vault_files WHERE vaultId = :vaultId")
    suspend fun getTotalSizeInVault(vaultId: String): Long?
    
    @Query("SELECT * FROM vault_files WHERE checksum = :checksum")
    suspend fun getFileByChecksum(checksum: String): VaultFileEntity?
    
    @Query("SELECT DISTINCT fileType FROM vault_files WHERE vaultId = :vaultId")
    suspend fun getFileTypesInVault(vaultId: String): List<FileType>
}

