package com.example.beingsober.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.data.local.entity.ChallengeEntity
import com.example.beingsober.domain.analyzer.PatternResult
import com.example.beingsober.domain.streak.getStreakMessage
import com.example.beingsober.domain.streak.getStreakMilestone
import com.example.beingsober.ui.challenge.ChallengeViewModel
import com.example.beingsober.ui.incident.IncidentViewModel
import java.util.Calendar

private val Background = Color(0xFF070707)
private val Surface = Color(0xFF121212)
private val SurfaceElevated = Color(0xFF181818)
private val Accent = Color(0xFFFF3B30)
private val AccentSoft = Color(0xFF24100F)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF929292)
private val MutedText = Color(0xFF656565)
private val Success = Color(0xFF69D391)

@Composable
fun HomeScreen(
    habitType: String?,
    onNewIncident: () -> Unit = {},
    onPatterns: () -> Unit = {},
    onEvidence: () -> Unit = {},
    onPlan: () -> Unit = {},
    onStatistics: () -> Unit = {},
    onUrgeBreak: () -> Unit = {},
    onCheckIn: () -> Unit = {},
    onChallenge: () -> Unit = {},
    viewModel: IncidentViewModel = hiltViewModel(),
    challengeViewModel: ChallengeViewModel = hiltViewModel()
) {
    val incidents = viewModel.incidents.collectAsState().value
    val detectedPattern = viewModel.detectedPattern.collectAsState().value
    val streakResult = viewModel.streakResult.collectAsState().value
    val latestChallenge =
        challengeViewModel.latestChallenge.collectAsState().value

    val incidentCount = incidents.size

    val startOfToday = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val todayIncidents = incidents.filter {
        it.timestamp >= startOfToday
    }

    val todayResisted = todayIncidents.count {
        it.wasResisted
    }

    val todaySetbacks = todayIncidents.count {
        !it.wasResisted
    }

    val todayResistanceRate =
        if (todayIncidents.isNotEmpty()) {
            (todayResisted * 100) / todayIncidents.size
        } else {
            0
        }

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
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BEING SOBER",
                        color = Accent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your recovery space",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SurfaceElevated)
                        .padding(11.dp)
                ) {
                    Text(
                        text = "✦",
                        color = Accent,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Find the trigger.",
                color = PrimaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Break the cycle.",
                color = SecondaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            RecoveryHeroCard(
                currentStreak = streakResult.currentStreak,
                longestStreak = streakResult.longestStreak
            )

            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(
                title = "TODAY",
                subtitle = "A quick look at your recovery"
            )

            Spacer(modifier = Modifier.height(10.dp))

            TodayCard(
                todayCount = todayIncidents.size,
                todayResisted = todayResisted,
                todaySetbacks = todaySetbacks,
                resistanceRate = todayResistanceRate
            )

            Spacer(modifier = Modifier.height(18.dp))

            RecoverySignalCard(
                detectedPattern = detectedPattern,
                onClick = onPatterns
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(
                title = "QUICK ACTIONS",
                subtitle = "Choose what you need right now"
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuickActionCard(
                title = "New Investigation",
                subtitle = "Understand what triggered the urge",
                symbol = "+",
                primary = true,
                onClick = onNewIncident
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallActionCard(
                    title = "Calm Drop",
                    subtitle = "30 sec reset",
                    symbol = "◎",
                    modifier = Modifier.weight(1f),
                    onClick = onUrgeBreak
                )

                SmallActionCard(
                    title = "Daily Check-In",
                    subtitle = "How are you?",
                    symbol = "✓",
                    modifier = Modifier.weight(1f),
                    onClick = onCheckIn
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(
                title = "THE CHALLENGE",
                subtitle = "Turn resistance into something real"
            )

            Spacer(modifier = Modifier.height(10.dp))

            ChallengeHomeCard(
                challenge = latestChallenge,
                onClick = onChallenge
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(
                title = "YOUR RECOVERY",
                subtitle = "Explore your progress"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecoveryNavigationCard(
                title = "Evidence",
                subtitle = "$incidentCount recorded ${
                    if (incidentCount == 1) "case" else "cases"
                }",
                symbol = "▣",
                onClick = onEvidence
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecoveryNavigationCard(
                title = "Patterns",
                subtitle = if (detectedPattern != null) {
                    "A recurring trigger was found"
                } else {
                    "Keep investigating to reveal patterns"
                },
                symbol = "⌁",
                onClick = onPatterns
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecoveryNavigationCard(
                title = "Recovery Plan",
                subtitle = if (detectedPattern != null) {
                    "Your response plan is ready"
                } else {
                    "Build evidence to create a plan"
                },
                symbol = "↗",
                onClick = onPlan
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecoveryNavigationCard(
                title = "Statistics",
                subtitle = "See your recovery numbers",
                symbol = "◒",
                onClick = onStatistics
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Small steps. Clear patterns. Better choices.",
                color = MutedText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 4.dp,
                        vertical = 4.dp
                    )
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun RecoveryHeroCard(
    currentStreak: Int,
    longestStreak: Int
) {
    val message = getStreakMessage(currentStreak)
    val milestone = getStreakMilestone(currentStreak)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "CURRENT STREAK",
                    color = SecondaryText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$currentStreak",
                        color = PrimaryText,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " days",
                        color = Accent,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            bottom = 10.dp,
                            start = 5.dp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AccentSoft)
                    .padding(14.dp)
            ) {
                Text(
                    text = "↗",
                    color = Accent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            color = SecondaryText,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Longest: $longestStreak days",
                color = MutedText,
                fontSize = 11.sp
            )

            if (milestone != null) {
                Text(
                    text = "✦ $milestone",
                    color = Accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            color = PrimaryText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.6.sp
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            color = MutedText,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TodayCard(
    todayCount: Int,
    todayResisted: Int,
    todaySetbacks: Int,
    resistanceRate: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TodayStat(
                value = todayCount.toString(),
                label = "Investigated"
            )

            TodayStat(
                value = todayResisted.toString(),
                label = "Resisted"
            )

            TodayStat(
                value = todaySetbacks.toString(),
                label = "Setbacks"
            )

            TodayStat(
                value = "$resistanceRate%",
                label = "Resistance",
                accent = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF262626))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        resistanceRate.coerceIn(0, 100) / 100f
                    )
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Accent)
            )
        }
    }
}

@Composable
private fun TodayStat(
    value: String,
    label: String,
    accent: Boolean = false
) {
    Column {
        Text(
            text = value,
            color = if (accent) Accent else PrimaryText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = MutedText,
            fontSize = 9.sp
        )
    }
}

@Composable
private fun RecoverySignalCard(
    detectedPattern: PatternResult?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AccentSoft,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "RECOVERY SIGNAL",
                    color = Accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (detectedPattern == null) {
                    Text(
                        text = "Your pattern is still forming.",
                        color = PrimaryText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Keep investigating. Repeated triggers will appear here.",
                        color = SecondaryText,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = detectedPattern.trigger,
                        color = PrimaryText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "appears repeatedly before ${
                            detectedPattern.habitType.lowercase()
                        }",
                        color = SecondaryText,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${detectedPattern.count} times  •  average urge ${
                            String.format(
                                "%.1f",
                                detectedPattern.averageUrge
                            )
                        }/10",
                        color = Accent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "→",
                color = Accent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    symbol: String,
    primary: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (primary) Accent else Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 18.dp,
                vertical = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (primary) {
                        Color.White.copy(alpha = 0.16f)
                    } else {
                        SurfaceElevated
                    }
                )
                .padding(10.dp)
        ) {
            Text(
                text = symbol,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = if (primary) {
                    Color.White.copy(alpha = 0.72f)
                } else {
                    SecondaryText
                },
                fontSize = 11.sp
            )
        }

        Text(
            text = "→",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SmallActionCard(
    title: String,
    subtitle: String,
    symbol: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(17.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceElevated)
                .padding(9.dp)
        ) {
            Text(
                text = symbol,
                color = Accent,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = title,
            color = PrimaryText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            color = MutedText,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RecoveryNavigationCard(
    title: String,
    subtitle: String,
    symbol: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 17.dp,
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceElevated)
                .padding(10.dp)
        ) {
            Text(
                text = symbol,
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(7.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = MutedText,
                fontSize = 10.sp
            )
        }

        Text(
            text = "→",
            color = SecondaryText,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun ChallengeHomeCard(
    challenge: ChallengeEntity?,
    onClick: () -> Unit
) {
    if (challenge == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable {
                    onClick()
                }
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "YOUR MONEY. YOUR GOAL.",
                        color = Accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.7.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Turn resisted urges into potential savings.",
                        color = PrimaryText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Choose something meaningful and make every resistance count.",
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "₹",
                    color = Success,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "START YOUR CHALLENGE",
                    color = PrimaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "→",
                    color = Accent,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        val savings =
            challenge.resistedCount * challenge.costPerUrge

        val progress =
            if (challenge.goalAmount > 0) {
                (savings / challenge.goalAmount)
                    .coerceIn(0.0, 1.0)
                    .toFloat()
            } else {
                0f
            }

        val percentage =
            (progress * 100).toInt()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable {
                    onClick()
                }
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "ACTIVE CHALLENGE",
                        color = Accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.7.sp
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = challenge.goalName,
                        color = PrimaryText,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${challenge.resistedCount} urges resisted",
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "₹",
                    color = Success,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = "₹${"%,.0f".format(savings)} / ₹${
                    "%,.0f".format(
                        challenge.goalAmount
                    )
                }",
                color = Success,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF292929))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Success)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "$percentage% COMPLETE",
                color = MutedText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(17.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CONTINUE CHALLENGE",
                    color = PrimaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "→",
                    color = Accent,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF070707
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        habitType = "BOTH"
    )
}