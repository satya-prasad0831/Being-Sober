package com.example.beingsober.ui.patterns

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beingsober.data.local.entity.IncidentEntity

@Composable
fun PatternsScreen(
    incidents: List<IncidentEntity>,
    onBack: () -> Unit
) {

    val groupedPatterns = incidents
        .groupBy {
            Pair(
                it.habitType,
                it.trigger
            )
        }
        .toList()
        .sortedByDescending {
            it.second.size
        }

    val totalIncidents = incidents.size

    val averageUrge =
        if (incidents.isNotEmpty()) {
            incidents
                .map { it.urgeLevel }
                .average()
        } else {
            0.0
        }

    val resistedCount =
        incidents.count {
            it.wasResisted
        }

    val resistanceRate =
        if (incidents.isNotEmpty()) {
            (resistedCount * 100) / incidents.size
        } else {
            0
        }

    val strongestTrigger =
        groupedPatterns
            .firstOrNull()
            ?.first
            ?.second
            ?: "None yet"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "← BACK",
                    color = Color(0xFFFF3B30),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )

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
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "PATTERN FILE",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Repeated evidence from your investigations.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                if (incidents.isNotEmpty()) {

                    PatternSummaryCard(
                        totalIncidents = totalIncidents,
                        strongestTrigger = strongestTrigger,
                        averageUrge = averageUrge,
                        resistanceRate = resistanceRate
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            if (groupedPatterns.isEmpty()) {

                item {

                    PatternEmptyState()
                }

            } else {

                items(groupedPatterns) { pattern ->

                    PatternCard(
                        habitType = pattern.first.first,
                        trigger = pattern.first.second,
                        incidents = pattern.second
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PatternSummaryCard(
    totalIncidents: Int,
    strongestTrigger: String,
    averageUrge: Double,
    resistanceRate: Int
) {

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
            text = "🔎 YOUR SUMMARY",
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

            SummaryValue(
                value = "$totalIncidents",
                label = "incidents"
            )

            SummaryValue(
                value = strongestTrigger,
                label = "strongest trigger"
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            SummaryValue(
                value = String.format(
                    "%.1f/10",
                    averageUrge
                ),
                label = "average urge"
            )

            SummaryValue(
                value = "$resistanceRate%",
                label = "resisted"
            )
        }
    }
}

@Composable
private fun SummaryValue(
    value: String,
    label: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp)
    ) {

        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PatternCard(
    habitType: String,
    trigger: String,
    incidents: List<IncidentEntity>
) {

    val averageUrge =
        incidents
            .map { it.urgeLevel }
            .average()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "🔎 $habitType",
            color = Color(0xFFFF3B30),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = trigger,
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "${incidents.size}",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "times seen",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Column {

                Text(
                    text = String.format(
                        "%.1f/10",
                        averageUrge
                    ),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "average urge",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun PatternEmptyState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFF2A2A2A),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "🔎 No patterns yet",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Keep recording incidents. Repeated triggers will appear here.",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}