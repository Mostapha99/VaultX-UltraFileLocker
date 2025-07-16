package com.vaultx.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.vaultx.data.database.dao.VaultDao
import com.vaultx.data.database.dao.VaultFileDao
import com.vaultx.data.database.dao.IntruderLogDao
import com.vaultx.data.database.entities.VaultEntity
import com.vaultx.data.database.entities.VaultFileEntity
import com.vaultx.data.database.entities.IntruderLogEntity
import com.vaultx.data.database.converters.DateConverter
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        VaultEntity::class,
        VaultFileEntity::class,
        IntruderLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class VaultDatabase : RoomDatabase() {
    
    abstract fun vaultDao(): VaultDao
    abstract fun vaultFileDao(): VaultFileDao
    abstract fun intruderLogDao(): IntruderLogDao
    
    companion object {
        @Volatile
        private var INSTANCE: VaultDatabase? = null
        
        fun getDatabase(context: Context, passphrase: String): VaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VaultDatabase::class.java,
                    "vault_database.db"
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

