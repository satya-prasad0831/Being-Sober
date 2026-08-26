package com.example.beingsober.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beingsober.data.local.dao.IncidentDao
import com.example.beingsober.data.local.entity.IncidentEntity

@Database(
    entities = [IncidentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BeingSoberDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao
}