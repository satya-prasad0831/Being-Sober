package com.example.beingsober.domain.streak

fun getStreakMessage(
    streak: Int
): String {

    return when {
        streak <= 0 ->
            "The investigation starts again. One decision at a time."

        streak == 1 ->
            "First day. That's where change begins."

        streak < 3 ->
            "You're breaking the automatic cycle."

        streak < 7 ->
            "Keep going. You're proving the pattern can be broken."

        streak < 14 ->
            "One week and counting. Stay aware of your triggers."

        streak < 30 ->
            "You're building distance between the urge and the action."

        streak < 60 ->
            "The old pattern is losing its grip. Keep investigating."

        else ->
            "You've built serious momentum. Keep protecting it."
    }
}