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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.ui.incident.IncidentViewModel
import com.example.beingsober.domain.streak.getStreakMessage
import com.example.beingsober.domain.streak.getStreakMilestone

@Composable
fun HomeScreen(
    habitType: String?,
    onNewIncident: () -> Unit = {},
    onPatterns: () -> Unit = {},
    onEvidence: () -> Unit = {},
    onPlan: () -> Unit = {},
    viewModel: IncidentViewModel = hiltViewModel()
) {


    val incidents = viewModel.incidents.collectAsState().value


    val incidentCount = incidents.size


    val detectedPattern =
        viewModel.detectedPattern.collectAsState().value

    val streakResult =
        viewModel.streakResult.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "BEING SOBER",
                color = Color(0xFFFF3B30),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Find the trigger.\nBreak the cycle.",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "YOUR INVESTIGATION",
                color = Color(0xFFFF3B30),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            HabitTypeDisplay(
                habitType = habitType
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "$incidentCount CASE${if (incidentCount == 1) "" else "S"}",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Investigated so far",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            StreakCard(
                currentStreak = streakResult.currentStreak,
                longestStreak = streakResult.longestStreak,
                smokingStreak = streakResult.smokingStreak,
                drinkingStreak = streakResult.drinkingStreak
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            NewIncidentCard(
                onClick = onNewIncident
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // -------------------------
            // LATEST CLUE
            // -------------------------

            Text(
                text = "LATEST CLUE",
                color = Color(0xFFFF3B30),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFFF3B30),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {

                if (detectedPattern == null) {

                    Column {

                        Text(
                            text = "🔎  Not enough evidence",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Record at least two similar incidents to reveal a pattern.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                } else {

                    Column {

                        Text(
                            text = "🔎  CLUE DETECTED",
                            color = Color(0xFFFF3B30),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "${detectedPattern.trigger} appears repeatedly before ${detectedPattern.habitType.lowercase()}.",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = "Seen ${detectedPattern.count} times",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Average urge: ${
                                String.format(
                                    "%.1f",
                                    detectedPattern.averageUrge
                                )
                            }/10",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                BottomItem(
                    text = "Evidence",
                    onClick = onEvidence
                )

                BottomItem(
                    text = "Patterns",
                    onClick = onPatterns
                )

                BottomItem(
                    text = "Plan",
                    onClick = onPlan
                )
            }
        }
    }
}

@Composable
private fun HabitTypeDisplay(
    habitType: String?
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        if (habitType == "SMOKING" || habitType == "BOTH") {

            HabitChip(
                text = "🚬 Smoking",
                modifier = Modifier.weight(1f)
            )
        }

        if (habitType == "DRINKING" || habitType == "BOTH") {

            HabitChip(
                text = "🍺 Drinking",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HabitChip(
    text: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun StreakCard(
    currentStreak: Int,
    longestStreak: Int,
    smokingStreak: Int,
    drinkingStreak: Int
) {

    val message = getStreakMessage(currentStreak)
    val milestone = getStreakMilestone(currentStreak)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF120000),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "🔥 RECOVERY STREAK",
            color = Color(0xFFFF3B30),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            StreakValue(
                title = "🚬 Smoking",
                days = smokingStreak
            )

            StreakValue(
                title = "🍺 Drinking",
                days = drinkingStreak
            )

            StreakValue(
                title = "🔥 Overall",
                days = currentStreak
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Longest overall: $longestStreak days",
            color = Color.Gray,
            fontSize = 13.sp
        )
        if (milestone != null) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "🏆 $milestone",
                color = Color(0xFFFF3B30),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = message,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun StreakValue(
    title: String,
    days: Int
) {

    Column {

        Text(
            text = "$days",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "days",
            color = Color(0xFFFF3B30),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun NewIncidentCard(
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF120000),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFFF3B30),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(22.dp)
    ) {

        Column {

            Text(
                text = "🔴  NEW INCIDENT",
                color = Color(0xFFFF3B30),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Something happened?",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Let's investigate it.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun BottomItem(
    text: String,
    onClick: () -> Unit = {}
) {

    Text(
        text = text,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(8.dp),
        color = Color(0xFFFF3B30),
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    HomeScreen(
        habitType = "BOTH"
    )
}