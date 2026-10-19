package com.example.beingsober.ui.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beingsober.data.local.entity.ChallengeEntity
import com.example.beingsober.data.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {

    val latestChallenge: StateFlow<ChallengeEntity?> =
        repository
            .getLatestChallenge()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun createChallenge(
        goalName: String,
        goalAmount: Double,
        costPerUrge: Double
    ) {
        viewModelScope.launch {
            repository.createChallenge(
                goalName = goalName,
                goalAmount = goalAmount,
                costPerUrge = costPerUrge
            )
        }
    }

    fun recordResistance() {
        viewModelScope.launch {

            val challenge =
                latestChallenge.value
                    ?: return@launch

            if (!challenge.isActive) {
                return@launch
            }

            val newCount =
                challenge.resistedCount + 1

            val newSavings =
                newCount * challenge.costPerUrge

            val completed =
                newSavings >= challenge.goalAmount

            repository.updateChallenge(
                challenge.copy(
                    resistedCount = newCount,
                    isActive = !completed
                )
            )
        }
    }

    fun deleteChallenge() {
        viewModelScope.launch {

            val challenge =
                latestChallenge.value
                    ?: return@launch

            repository.deleteChallengeById(
                challenge.id
            )
        }
    }
}