package com.example.beingsober.domain.streak

import com.example.beingsober.data.local.entity.IncidentEntity
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

data class StreakResult(
    val currentStreak: Int,
    val longestStreak: Int,
    val smokingStreak: Int,
    val drinkingStreak: Int
)

class StreakCalculator @Inject constructor() {

    fun calculate(
        incidents: List<IncidentEntity>
    ): StreakResult {

        if (incidents.isEmpty()) {
            return StreakResult(
                currentStreak = 0,
                longestStreak = 0,
                smokingStreak = 0,
                drinkingStreak = 0
            )
        }

        val smokingIncidents = incidents.filter {
            it.habitType == "SMOKING" ||
                    it.habitType == "BOTH"
        }

        val drinkingIncidents = incidents.filter {
            it.habitType == "DRINKING" ||
                    it.habitType == "BOTH"
        }

        val smokingResult =
            calculateHabitStreak(smokingIncidents)

        val drinkingResult =
            calculateHabitStreak(drinkingIncidents)

        val overallResult =
            calculateHabitStreak(incidents)

        return StreakResult(
            currentStreak = overallResult.current,
            longestStreak = overallResult.longest,
            smokingStreak = smokingResult.current,
            drinkingStreak = drinkingResult.current
        )
    }

    private data class BasicStreak(
        val current: Int,
        val longest: Int
    )

    private fun calculateHabitStreak(
        incidents: List<IncidentEntity>
    ): BasicStreak {

        if (incidents.isEmpty()) {
            return BasicStreak(
                current = 0,
                longest = 0
            )
        }

        /*
         * Only non-resisted incidents are setbacks.
         */
        val setbackDates = incidents
            .filter {
                !it.wasResisted
            }
            .map {
                startOfDay(it.timestamp)
            }
            .distinct()
            .sorted()

        /*
         * No setbacks recorded.
         *
         * If the user has only resisted urges,
         * we count from the first recorded incident.
         */
        if (setbackDates.isEmpty()) {

            val firstDate =
                incidents
                    .minOf {
                        startOfDay(it.timestamp)
                    }

            val today =
                startOfDay(
                    System.currentTimeMillis()
                )

            val days =
                daysBetween(
                    firstDate,
                    today
                )

            return BasicStreak(
                current = days,
                longest = days
            )
        }

        val today =
            startOfDay(
                System.currentTimeMillis()
            )

        val latestSetback =
            setbackDates.last()

        /*
         * Current streak:
         *
         * If the latest setback happened today,
         * the current streak is zero.
         *
         * Otherwise, count clean days since
         * the latest setback.
         */
        val currentStreak =
            if (latestSetback == today) {

                0

            } else {

                daysBetween(
                    latestSetback,
                    today
                )
            }

        /*
         * Calculate the longest clean period
         * between setback dates.
         */
        var longestStreak =
            currentStreak

        for (index in 0 until setbackDates.lastIndex) {

            val previousSetback =
                setbackDates[index]

            val nextSetback =
                setbackDates[index + 1]

            val cleanDays =
                daysBetween(
                    previousSetback,
                    nextSetback
                ) - 1

            if (cleanDays > longestStreak) {

                longestStreak =
                    cleanDays
            }
        }

        /*
         * Also consider the period before
         * the first recorded setback.
         */
        val firstIncident =
            incidents.minOf {
                startOfDay(it.timestamp)
            }

        val firstSetback =
            setbackDates.first()

        val daysBeforeFirstSetback =
            daysBetween(
                firstIncident,
                firstSetback
            )

        if (daysBeforeFirstSetback > longestStreak) {

            longestStreak =
                daysBeforeFirstSetback
        }

        return BasicStreak(
            current = currentStreak,
            longest = longestStreak
        )
    }

    private fun startOfDay(
        timestamp: Long
    ): Long {

        val calendar =
            Calendar.getInstance()

        calendar.timeInMillis =
            timestamp

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        )

        calendar.set(
            Calendar.MINUTE,
            0
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )

        return calendar.timeInMillis
    }

    private fun daysBetween(
        first: Long,
        second: Long
    ): Int {

        return TimeUnit.MILLISECONDS
            .toDays(
                second - first
            )
            .toInt()
    }
}