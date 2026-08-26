package com.example.beingsober.domain.plan

data class RecoveryPlan(
    val title: String,
    val trigger: String,
    val steps: List<PlanStep>
)

data class PlanStep(
    val number: Int,
    val title: String,
    val description: String
)