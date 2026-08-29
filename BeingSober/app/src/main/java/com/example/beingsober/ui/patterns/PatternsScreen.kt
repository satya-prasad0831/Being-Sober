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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beingsober.data.local.entity.IncidentEntity

private val Background = Color(0xFF050505)
private val Surface = Color(0xFF111111)
private val SurfaceSoft = Color(0xFF151515)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)
private val BorderColor = Color(0xFF262626)

@Composable
fun PatternsScreen(
    incidents: List<IncidentEntity>,
    onBack: () -> Unit
) {

    val groupedPatterns =
        incidents
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

    val totalIncidents =
        incidents.size

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

            (resistedCount * 100) /
                    incidents.size

        } else {

            0
        }

    val strongestPattern =
        groupedPatterns.firstOrNull()

    val strongestTrigger =
        strongestPattern
            ?.first
            ?.second
            ?: "None yet"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 28.dp,
                    bottom = 30.dp
                )
        ) {
            item {

                Text(
                    text = "← BACK",
                    color = Accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "BEING SOBER",
                    color = Accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "PATTERN FILE",
                    color = PrimaryText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Repeated evidence from your investigations.",
                    color = SecondaryText,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )
            }
            if (incidents.isNotEmpty()) {

                item {

                    PatternSummaryCard(
                        totalIncidents = totalIncidents,
                        strongestTrigger = strongestTrigger,
                        averageUrge = averageUrge,
                        resistanceRate = resistanceRate
                    )

                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )
                }
            }
            item {

                Text(
                    text = "REPEATED PATTERNS",
                    color = SecondaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }
            if (groupedPatterns.isEmpty()) {

                item {

                    PatternEmptyState()
                }

            } else {

                items(groupedPatterns) { pattern ->

                    PatternCard(
                        habitType =
                            pattern.first.first,

                        trigger =
                            pattern.first.second,

                        incidents =
                            pattern.second
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
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
                color = SurfaceSoft,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(22.dp)
    ) {

        Text(
            text = "YOUR RECOVERY DATA",
            color = Accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            SummaryValue(
                value = totalIncidents.toString(),
                label = "INCIDENTS",
                modifier = Modifier.weight(1f)
            )

            SummaryValue(
                value = "$resistanceRate%",
                label = "RESISTED",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            SummaryValue(
                value = String.format(
                    "%.1f/10",
                    averageUrge
                ),
                label = "AVG URGE",
                modifier = Modifier.weight(1f)
            )

            SummaryValue(
                value = strongestTrigger,
                label = "TOP TRIGGER",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryValue(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {

        Text(
            text = value,
            color = PrimaryText,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = SecondaryText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
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
            .map {
                it.urgeLevel
            }
            .average()

    val count =
        incidents.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "🔎 $habitType",
            color = Accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )


        Text(
            text = trigger,
            color = PrimaryText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.Bottom
        ) {

            Column {

                Text(
                    text = "$count",
                    color = PrimaryText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "TIMES SEEN",
                    color = SecondaryText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text = String.format(
                        "%.1f/10",
                        averageUrge
                    ),
                    color = Accent,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "AVERAGE URGE",
                    color = SecondaryText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    color = BorderColor,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        (count.coerceAtMost(10)) / 10f
                    )
                    .height(4.dp)
                    .background(
                        color = Accent,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}


@Composable
private fun PatternEmptyState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(22.dp)
    ) {

        Text(
            text = "🔎 NO PATTERNS YET",
            color = PrimaryText,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Keep recording incidents. Repeated triggers will appear here as your recovery data grows.",
            color = SecondaryText,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}