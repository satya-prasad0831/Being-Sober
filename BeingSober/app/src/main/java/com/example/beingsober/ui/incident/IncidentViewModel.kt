package com.example.beingsober.ui.incident

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beingsober.data.local.entity.IncidentEntity
import com.example.beingsober.data.repository.IncidentRepository
import com.example.beingsober.domain.analyzer.PatternAnalyzer
import com.example.beingsober.domain.analyzer.PatternResult
import com.example.beingsober.domain.plan.PlanGenerator
import com.example.beingsober.domain.plan.RecoveryPlan
import com.example.beingsober.domain.streak.StreakCalculator
import com.example.beingsober.domain.streak.StreakResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentViewModel @Inject constructor(
    private val repository: IncidentRepository,
    private val patternAnalyzer: PatternAnalyzer,
    private val streakCalculator: StreakCalculator,
    private val planGenerator: PlanGenerator
) : ViewModel() {

    val incidents: StateFlow<List<IncidentEntity>> =
        repository.getAllIncidents()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val detectedPattern: StateFlow<PatternResult?> =
        incidents
            .map { incidentList ->
                patternAnalyzer.analyze(incidentList)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val recoveryPlan =
        detectedPattern.map { pattern ->

            pattern?.let {

                planGenerator.generate(
                    trigger = it.trigger,
                    habitType = it.habitType,
                    incidentCount = it.count,
                    averageUrge = it.averageUrge
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = planGenerator.generate(
                    trigger = null,
                    habitType = null
                )
            )

    val streakResult: StateFlow<StreakResult> =
        incidents
            .map { incidentList ->
                streakCalculator.calculate(incidentList)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = StreakResult(
                    currentStreak = 0,
                    longestStreak = 0,
                    smokingStreak = 0,
                    drinkingStreak = 0
                )
            )

    fun saveIncident(
        habitType: String,
        urgeLevel: Int,
        trigger: String,
        location: String,
        notes: String,
        wasResisted: Boolean
    ) {

        val incident = IncidentEntity(
            habitType = habitType,
            timestamp = System.currentTimeMillis(),
            urgeLevel = urgeLevel,
            trigger = trigger,
            location = location,
            notes = notes,
            wasResisted = wasResisted
        )

        viewModelScope.launch {
            repository.insertIncident(incident)
        }
    }
}