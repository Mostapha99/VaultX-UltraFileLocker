package com.vaultx.data.database.dao

import androidx.room.*
import com.vaultx.data.database.entities.VaultEntity
import com.vaultx.data.database.entities.VaultType
import kotlinx.coroutines.flow.Flow

@Dao
interface VaultDao {
    
    @Query("SELECT * FROM vaults WHERE isHidden = 0 ORDER BY updatedAt DESC")
    fun getAllVaults(): Flow<List<VaultEntity>>
    
    @Query("SELECT * FROM vaults WHERE isHidden = 1 ORDER BY updatedAt DESC")
    fun getHiddenVaults(): Flow<List<VaultEntity>>
    
    @Query("SELECT * FROM vaults WHERE isFakeVault = 1 ORDER BY updatedAt DESC")
    fun getFakeVaults(): Flow<List<VaultEntity>>
    
    @Query("SELECT * FROM vaults WHERE id = :vaultId")
    suspend fun getVaultById(vaultId: String): VaultEntity?
    
    @Query("SELECT * FROM vaults WHERE vaultType = :type ORDER BY updatedAt DESC")
    fun getVaultsByType(type: VaultType): Flow<List<VaultEntity>>
    
    @Query("SELECT * FROM vaults WHERE name LIKE '%' || :searchQuery || '%' ORDER BY updatedAt DESC")
    fun searchVaults(searchQuery: String): Flow<List<VaultEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVault(vault: VaultEntity)
    
    @Update
    suspend fun updateVault(vault: VaultEntity)
    
    @Delete
    suspend fun deleteVault(vault: VaultEntity)
    
    @Query("DELETE FROM vaults WHERE id = :vaultId")
    suspend fun deleteVaultById(vaultId: String)
    
    @Query("UPDATE vaults SET fileCount = :count, totalSize = :size, updatedAt = :updatedAt WHERE id = :vaultId")
    suspend fun updateVaultStats(vaultId: String, count: Int, size: Long, updatedAt: java.util.Date)
    
    @Query("SELECT COUNT(*) FROM vaults")
    suspend fun getVaultCount(): Int
    
    @Query("SELECT SUM(totalSize) FROM vaults")
    suspend fun getTotalSize(): Long?
}

