package com.example.beingsober.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.domain.analyzer.PatternResult
import com.example.beingsober.domain.streak.getStreakMessage
import com.example.beingsober.domain.streak.getStreakMilestone
import com.example.beingsober.ui.incident.IncidentViewModel
import java.util.Calendar

private val Background = Color(0xFF050505)
private val Surface = Color(0xFF111111)
private val SurfaceSoft = Color(0xFF151515)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)
private val BorderColor = Color(0xFF262626)

@Composable
fun HomeScreen(
    habitType: String?,
    onNewIncident: () -> Unit = {},
    onPatterns: () -> Unit = {},
    onEvidence: () -> Unit = {},
    onPlan: () -> Unit = {},
    onStatistics: () -> Unit = {},
    viewModel: IncidentViewModel = hiltViewModel()
) {

    val incidents =
        viewModel.incidents.collectAsState().value

    val detectedPattern =
        viewModel.detectedPattern.collectAsState().value

    val streakResult =
        viewModel.streakResult.collectAsState().value

    val incidentCount = incidents.size

    // -----------------------------
    // TODAY'S DATA
    // -----------------------------

    val startOfToday =
        Calendar.getInstance().apply {

            set(
                Calendar.HOUR_OF_DAY,
                0
            )

            set(
                Calendar.MINUTE,
                0
            )

            set(
                Calendar.SECOND,
                0
            )

            set(
                Calendar.MILLISECOND,
                0
            )

        }.timeInMillis

    val todayIncidents =
        incidents.filter {
            it.timestamp >= startOfToday
        }

    val todayResisted =
        todayIncidents.count {
            it.wasResisted
        }

    val todaySetbacks =
        todayIncidents.count {
            !it.wasResisted
        }

    val todayResistanceRate =
        if (todayIncidents.isNotEmpty()) {

            (todayResisted * 100) /
                    todayIncidents.size

        } else {

            0
        }

    // -----------------------------
    // HOME
    // -----------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 30.dp,
                    bottom = 30.dp
                )
        ) {

            // -----------------------------
            // HEADER
            // -----------------------------

            Text(
                text = "BEING SOBER",
                color = Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Find the trigger.\nBreak the cycle.",
                color = PrimaryText,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 39.sp
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            // -----------------------------
            // STREAK
            // -----------------------------

            MainStreakCard(
                currentStreak =
                    streakResult.currentStreak,

                longestStreak =
                    streakResult.longestStreak
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // -----------------------------
            // TODAY
            // -----------------------------

            TodayRecoveryCard(
                todayCount =
                    todayIncidents.size,

                todayResisted =
                    todayResisted,

                todaySetbacks =
                    todaySetbacks,

                resistanceRate =
                    todayResistanceRate
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // -----------------------------
            // INSIGHT
            // -----------------------------

            InsightCard(
                detectedPattern =
                    detectedPattern
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // -----------------------------
            // NEW INVESTIGATION
            // -----------------------------

            InvestigationButton(
                onClick =
                    onNewIncident
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            // -----------------------------
            // NAVIGATION
            // -----------------------------

            Text(
                text = "EXPLORE YOUR RECOVERY",
                color = SecondaryText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                NavigationCard(
                    title = "Evidence",
                    subtitle =
                        "$incidentCount cases",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onEvidence
                )

                NavigationCard(
                    title = "Patterns",
                    subtitle =
                        if (detectedPattern != null) {
                            "Insight found"
                        } else {
                            "Keep investigating"
                        },
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onPatterns
                )

                NavigationCard(
                    title = "Plan",
                    subtitle =
                        if (detectedPattern != null) {
                            "Ready"
                        } else {
                            "Build evidence"
                        },
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onPlan
                )
            }
            Spacer(
                modifier = Modifier.height(10.dp)
            )

            NavigationCard(
                title = "Statistics",
                subtitle = "Your recovery data",
                modifier = Modifier.fillMaxWidth(),
                onClick = onStatistics
            )
        }
    }
}
@Composable
private fun MainStreakCard(
    currentStreak: Int,
    longestStreak: Int
) {

    val message =
        getStreakMessage(currentStreak)

    val milestone =
        getStreakMilestone(currentStreak)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {

        Text(
            text = "CURRENT STREAK",
            color = SecondaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Row(
            verticalAlignment =
                Alignment.Bottom
        ) {

            Text(
                text = "$currentStreak",
                color = PrimaryText,
                fontSize = 58.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " DAYS",
                color = Accent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    bottom = 11.dp
                )
            )
        }

        Text(
            text = message,
            color = SecondaryText,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "Longest: $longestStreak days",
                color = SecondaryText,
                fontSize = 12.sp
            )

            if (milestone != null) {

                Text(
                    text = "🏆 $milestone",
                    color = Accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Composable
private fun TodayRecoveryCard(
    todayCount: Int,
    todayResisted: Int,
    todaySetbacks: Int,
    resistanceRate: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "TODAY",
            color = Accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            RecoveryStat(
                value = todayCount.toString(),
                label = "investigated"
            )

            RecoveryStat(
                value = todayResisted.toString(),
                label = "resisted"
            )

            RecoveryStat(
                value = todaySetbacks.toString(),
                label = "setbacks"
            )

            RecoveryStat(
                value = "$resistanceRate%",
                label = "resistance"
            )
        }
    }
}
@Composable
private fun RecoveryStat(
    value: String,
    label: String
) {

    Column {

        Text(
            text = value,
            color = PrimaryText,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = label,
            color = SecondaryText,
            fontSize = 10.sp
        )
    }
}
@Composable
private fun InsightCard(
    detectedPattern: PatternResult?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SurfaceSoft,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "RECOVERY SIGNAL",
            color = SecondaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (detectedPattern == null) {

            Text(
                text = "Your pattern is still forming.",
                color = PrimaryText,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Keep investigating. Repeated triggers will reveal themselves here.",
                color = SecondaryText,
                fontSize = 13.sp
            )

        } else {

            Text(
                text = detectedPattern.trigger,
                color = PrimaryText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "appears repeatedly before ${detectedPattern.habitType.lowercase()}",
                color = SecondaryText,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text =
                    "${detectedPattern.count} times  •  average urge ${
                        String.format(
                            "%.1f",
                            detectedPattern.averageUrge
                        )
                    }/10",
                color = Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
private fun InvestigationButton(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Accent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 22.dp,
                vertical = 19.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "NEW INVESTIGATION",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Something happened? Understand it.",
                    color = Color.White.copy(
                        alpha = 0.75f
                    ),
                    fontSize = 12.sp
                )
            }

            Text(
                text = "→",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
private fun NavigationCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .background(
                color = Surface,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {

        Text(
            text = title,
            color = PrimaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = subtitle,
            color = SecondaryText,
            fontSize = 10.sp
        )
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    HomeScreen(
        habitType = "BOTH"
    )
}