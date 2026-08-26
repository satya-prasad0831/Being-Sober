package com.example.beingsober.domain.streak

fun getStreakMilestone(streak: Int): String? {

    return when (streak) {
        1 -> "FIRST DAY"
        3 -> "3 DAY MILESTONE"
        7 -> "ONE WEEK"
        14 -> "TWO WEEKS"
        30 -> "30 DAY MILESTONE"
        60 -> "60 DAY MILESTONE"
        90 -> "90 DAY MILESTONE"
        else -> null
    }
}