package com.example.beingsober.ui.statistics

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beingsober.data.local.entity.IncidentEntity

private val Background = Color(0xFF050505)
private val Surface = Color(0xFF111111)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)
private val BorderColor = Color(0xFF262626)

@Composable
fun StatisticsScreen(
    incidents: List<IncidentEntity>,
    longestStreak: Int,
    onBack: () -> Unit
) {

    val total = incidents.size

    val resisted = incidents.count {
        it.wasResisted
    }

    val setbacks = incidents.count {
        !it.wasResisted
    }

    val resistanceRate =
        if (total > 0) {
            (resisted * 100) / total
        } else {
            0
        }

    val averageUrge =
        if (total > 0) {
            incidents
                .map { it.urgeLevel }
                .average()
        } else {
            0.0
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
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 30.dp,
                    bottom = 30.dp
                )
        ) {

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
                text = "STATISTICS",
                color = Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Your recovery data.",
                color = PrimaryText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            StatisticsCard(
                title = "INVESTIGATIONS",
                value = total.toString(),
                description = "Total recorded cases"
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SmallStatisticsCard(
                    value = resisted.toString(),
                    label = "RESISTED",
                    modifier = Modifier.weight(1f)
                )

                SmallStatisticsCard(
                    value = setbacks.toString(),
                    label = "SETBACKS",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                SmallStatisticsCard(
                    value = "$resistanceRate%",
                    label = "RESISTANCE",
                    modifier = Modifier.weight(1f)
                )

                SmallStatisticsCard(
                    value = String.format(
                        "%.1f",
                        averageUrge
                    ),
                    label = "AVG URGE / 10",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            StatisticsCard(
                title = "LONGEST STREAK",
                value = "$longestStreak DAYS",
                description = "Your best recovery streak"
            )
        }
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    value: String,
    description: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = title,
            color = SecondaryText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = value,
            color = PrimaryText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = description,
            color = SecondaryText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SmallStatisticsCard(
    value: String,
    label: String,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {

        Text(
            text = value,
            color = PrimaryText,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = SecondaryText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}