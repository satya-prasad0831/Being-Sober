package com.example.beingsober.domain.plan

data class RecoveryPlan(
    val title: String,
    val trigger: String,
    val habitType: String,
    val incidentCount: Int,
    val averageUrge: Double,
    val steps: List<PlanStep>
)

data class PlanStep(
    val number: Int,
    val title: String,
    val description: String
)