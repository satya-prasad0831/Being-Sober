package com.example.beingsober.domain.plan
import javax.inject.Inject

class PlanGenerator @Inject constructor() {

    fun generate(
        trigger: String?,
        habitType: String?
    ): RecoveryPlan {

        val normalizedTrigger =
            trigger?.trim()?.uppercase() ?: "UNKNOWN"

        val habit =
            habitType?.trim()?.uppercase() ?: "UNKNOWN"

        return when {

            normalizedTrigger.contains("STRESS") ->
                stressPlan(habit)

            normalizedTrigger.contains("BOREDOM") ->
                boredomPlan(habit)

            normalizedTrigger.contains("SOCIAL") ->
                socialPlan(habit)

            normalizedTrigger.contains("ANGER") ->
                angerPlan(habit)

            normalizedTrigger.contains("LONELY") ||
                    normalizedTrigger.contains("LONELINESS") ->
                lonelinessPlan(habit)

            normalizedTrigger.contains("CRAVING") ->
                cravingPlan(habit)

            normalizedTrigger.contains("ROUTINE") ||
                    normalizedTrigger.contains("HABIT") ->
                routinePlan(habit)

            else ->
                generalPlan(habit)
        }
    }

    private fun stressPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "STRESS RESPONSE",
            trigger = "Stress",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "DELAY",
                    description = "Give yourself 10 minutes before acting on the urge."
                ),
                PlanStep(
                    number = 2,
                    title = "MOVE",
                    description = "Leave the environment where the stress response started."
                ),
                PlanStep(
                    number = 3,
                    title = "RESET",
                    description = "Change your activity and give your attention something else."
                ),
                PlanStep(
                    number = 4,
                    title = "INVESTIGATE",
                    description = "Record what happened and rate the urge again."
                )
            )
        )
    }

    private fun boredomPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "BOREDOM RESPONSE",
            trigger = "Boredom",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "IDENTIFY",
                    description = "Notice that boredom, not the habit itself, triggered the urge."
                ),
                PlanStep(
                    number = 2,
                    title = "SWITCH",
                    description = "Immediately change what you are doing."
                ),
                PlanStep(
                    number = 3,
                    title = "ENGAGE",
                    description = "Choose a short activity that keeps your attention occupied."
                ),
                PlanStep(
                    number = 4,
                    title = "RECHECK",
                    description = "After 10 minutes, rate the urge again."
                )
            )
        )
    }

    private fun socialPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "SOCIAL RESPONSE",
            trigger = "Social",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "PAUSE",
                    description = "Don't make the decision automatically because others are doing it."
                ),
                PlanStep(
                    number = 2,
                    title = "CREATE DISTANCE",
                    description = "Step away from the situation if the urge becomes difficult."
                ),
                PlanStep(
                    number = 3,
                    title = "REMEMBER",
                    description = "Remember why you chose to change this habit."
                ),
                PlanStep(
                    number = 4,
                    title = "RECHECK",
                    description = "Rate your urge again before deciding what to do."
                )
            )
        )
    }

    private fun angerPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "ANGER RESPONSE",
            trigger = "Anger",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "PAUSE",
                    description = "Don't act on the first impulse."
                ),
                PlanStep(
                    number = 2,
                    title = "STEP AWAY",
                    description = "Move away from the situation for a few minutes."
                ),
                PlanStep(
                    number = 3,
                    title = "RESET",
                    description = "Give yourself time to cool down before making a decision."
                ),
                PlanStep(
                    number = 4,
                    title = "INVESTIGATE",
                    description = "Record what triggered the anger and the urge."
                )
            )
        )
    }

    private fun lonelinessPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "LONELINESS RESPONSE",
            trigger = "Loneliness",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "RECOGNIZE",
                    description = "Notice that loneliness is driving the urge."
                ),
                PlanStep(
                    number = 2,
                    title = "CONNECT",
                    description = "Reach out to someone you trust."
                ),
                PlanStep(
                    number = 3,
                    title = "CHANGE ENVIRONMENT",
                    description = "Move somewhere that feels less isolating."
                ),
                PlanStep(
                    number = 4,
                    title = "RECHECK",
                    description = "Rate the urge again after the situation changes."
                )
            )
        )
    }

    private fun cravingPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "CRAVING RESPONSE",
            trigger = "Craving",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "DELAY",
                    description = "Wait 10 minutes before making a decision."
                ),
                PlanStep(
                    number = 2,
                    title = "DISTANCE",
                    description = "Move away from anything that makes the craving easier to act on."
                ),
                PlanStep(
                    number = 3,
                    title = "DISTRACT",
                    description = "Focus completely on another activity for a few minutes."
                ),
                PlanStep(
                    number = 4,
                    title = "RECHECK",
                    description = "Rate the craving again and record what changed."
                )
            )
        )
    }

    private fun routinePlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "ROUTINE BREAK",
            trigger = "Habit / Routine",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "NOTICE",
                    description = "Identify the moment when the automatic routine normally begins."
                ),
                PlanStep(
                    number = 2,
                    title = "BREAK",
                    description = "Change one part of the routine immediately."
                ),
                PlanStep(
                    number = 3,
                    title = "REPLACE",
                    description = "Choose another activity for that moment."
                ),
                PlanStep(
                    number = 4,
                    title = "RECORD",
                    description = "Record whether the urge became weaker."
                )
            )
        )
    }

    private fun generalPlan(
        habitType: String
    ): RecoveryPlan {

        return RecoveryPlan(
            title = "URGE RESPONSE",
            trigger = "Unknown",
            steps = listOf(
                PlanStep(
                    number = 1,
                    title = "PAUSE",
                    description = "Give yourself a moment before acting on the urge."
                ),
                PlanStep(
                    number = 2,
                    title = "MOVE",
                    description = "Change your surroundings if possible."
                ),
                PlanStep(
                    number = 3,
                    title = "DELAY",
                    description = "Wait 10 minutes and let the intensity change."
                ),
                PlanStep(
                    number = 4,
                    title = "INVESTIGATE",
                    description = "Record what happened so Being Sober can find the pattern."
                )
            )
        )
    }
}