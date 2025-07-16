package com.vaultx.data.database.dao

import androidx.room.*
import com.vaultx.data.database.entities.IntruderLogEntity
import com.vaultx.data.database.entities.AttemptType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface IntruderLogDao {
    
    @Query("SELECT * FROM intruder_logs ORDER BY timestamp DESC")
    fun getAllIntruderLogs(): Flow<List<IntruderLogEntity>>
    
    @Query("SELECT * FROM intruder_logs WHERE isResolved = 0 ORDER BY timestamp DESC")
    fun getUnresolvedLogs(): Flow<List<IntruderLogEntity>>
    
    @Query("SELECT * FROM intruder_logs WHERE attemptType = :type ORDER BY timestamp DESC")
    fun getLogsByType(type: AttemptType): Flow<List<IntruderLogEntity>>
    
    @Query("SELECT * FROM intruder_logs WHERE timestamp >= :since ORDER BY timestamp DESC")
    fun getLogsAfter(since: Date): Flow<List<IntruderLogEntity>>
    
    @Query("SELECT * FROM intruder_logs WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun getLogsBetween(start: Date, end: Date): Flow<List<IntruderLogEntity>>
    
    @Query("SELECT * FROM intruder_logs WHERE id = :logId")
    suspend fun getLogById(logId: String): IntruderLogEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: IntruderLogEntity)
    
    @Update
    suspend fun updateLog(log: IntruderLogEntity)
    
    @Delete
    suspend fun deleteLog(log: IntruderLogEntity)
    
    @Query("DELETE FROM intruder_logs WHERE id = :logId")
    suspend fun deleteLogById(logId: String)
    
    @Query("DELETE FROM intruder_logs WHERE timestamp < :before")
    suspend fun deleteOldLogs(before: Date)
    
    @Query("UPDATE intruder_logs SET isResolved = 1 WHERE id = :logId")
    suspend fun markAsResolved(logId: String)
    
    @Query("UPDATE intruder_logs SET isResolved = 1")
    suspend fun markAllAsResolved()
    
    @Query("SELECT COUNT(*) FROM intruder_logs WHERE isResolved = 0")
    suspend fun getUnresolvedCount(): Int
    
    @Query("SELECT COUNT(*) FROM intruder_logs WHERE timestamp >= :since")
    suspend fun getLogCountSince(since: Date): Int
    
    @Query("SELECT COUNT(*) FROM intruder_logs WHERE attemptType = :type AND timestamp >= :since")
    suspend fun getLogCountByTypeSince(type: AttemptType, since: Date): Int
}

