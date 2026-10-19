package com.example.beingsober.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class ChallengeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val goalName: String,

    val goalAmount: Double,

    val costPerUrge: Double,

    val resistedCount: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),

    val isActive: Boolean = true
)