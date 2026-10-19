package com.example.beingsober.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.beingsober.data.local.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Insert
    suspend fun insertChallenge(
        challenge: ChallengeEntity
    ): Long

    @Query(
        "SELECT * FROM challenges " +
                "WHERE isActive = 1 " +
                "ORDER BY createdAt DESC LIMIT 1"
    )
    fun getActiveChallenge(): Flow<ChallengeEntity?>

    @Query(
        "SELECT * FROM challenges " +
                "ORDER BY createdAt DESC LIMIT 1"
    )
    fun getLatestChallenge(): Flow<ChallengeEntity?>

    @Update
    suspend fun updateChallenge(
        challenge: ChallengeEntity
    )

    @Query(
        "UPDATE challenges SET isActive = 0 WHERE id = :id"
    )
    suspend fun deactivateChallenge(
        id: Int
    )

    @Delete
    suspend fun deleteChallenge(
        challenge: ChallengeEntity
    )

    @Query(
        "DELETE FROM challenges WHERE id = :id"
    )
    suspend fun deleteChallengeById(
        id: Int
    )
}