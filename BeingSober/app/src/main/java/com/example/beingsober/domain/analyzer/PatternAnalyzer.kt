package com.example.beingsober.domain.analyzer

import com.example.beingsober.data.local.entity.IncidentEntity
import javax.inject.Inject

data class PatternResult(
    val trigger: String,
    val habitType: String,
    val count: Int,
    val averageUrge: Double
)

class PatternAnalyzer @Inject constructor() {

    fun analyze(
        incidents: List<IncidentEntity>
    ): PatternResult? {

        if (incidents.size < 2) {
            return null
        }

        val grouped = incidents
            .filter {
                it.trigger.isNotBlank()
            }
            .groupBy {
                Pair(
                    it.habitType,
                    it.trigger
                )
            }
        if (grouped.isEmpty()) {
            return null
        }

        val strongestPattern = grouped
            .maxByOrNull { (_, cases) ->
                cases.size
            }

        if (strongestPattern == null) {
            return null
        }

        val (key, cases) = strongestPattern

        val averageUrge =
            cases.map { it.urgeLevel }.average()

        return PatternResult(
            trigger = key.second,
            habitType = key.first,
            count = cases.size,
            averageUrge = averageUrge
        )
    }
}