package com.example.beingsober.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val habitType: String,

    val timestamp: Long,

    val urgeLevel: Int,

    val trigger: String,

    val location: String,

    val notes: String,

    val wasResisted: Boolean = false
)