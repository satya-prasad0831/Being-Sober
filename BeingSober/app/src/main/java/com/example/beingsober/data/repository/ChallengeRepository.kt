package com.example.beingsober.data.repository

import com.example.beingsober.data.local.dao.ChallengeDao
import com.example.beingsober.data.local.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeDao: ChallengeDao
) {

    fun getActiveChallenge(): Flow<ChallengeEntity?> {
        return challengeDao.getActiveChallenge()
    }
    fun getLatestChallenge(): Flow<ChallengeEntity?> {
        return challengeDao.getLatestChallenge()
    }

    suspend fun createChallenge(
        goalName: String,
        goalAmount: Double,
        costPerUrge: Double
    ) {

        val challenge = ChallengeEntity(
            goalName = goalName,
            goalAmount = goalAmount,
            costPerUrge = costPerUrge
        )

        challengeDao.insertChallenge(challenge)
    }

    suspend fun updateChallenge(
        challenge: ChallengeEntity
    ) {
        challengeDao.updateChallenge(challenge)
    }

    suspend fun deactivateChallenge(
        id: Int
    ) {
        challengeDao.deactivateChallenge(id)
    }

    suspend fun deleteChallenge(
        challenge: ChallengeEntity
    ) {
        challengeDao.deleteChallenge(challenge)
    }

    suspend fun deleteChallengeById(
        id: Int
    ) {
        challengeDao.deleteChallengeById(id)
    }
}