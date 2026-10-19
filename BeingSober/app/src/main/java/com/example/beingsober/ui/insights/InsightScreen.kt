package com.example.beingsober.ui.insights

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.ui.incident.IncidentViewModel

private val Background = Color(0xFF070707)
private val Surface = Color(0xFF121212)
private val SurfaceElevated = Color(0xFF191919)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF929292)
private val MutedText = Color(0xFF656565)
private val Success = Color(0xFF69D391)

@Composable
fun InsightsScreen(
    onBack: () -> Unit = {},
    viewModel: IncidentViewModel = hiltViewModel()
) {

    val incidents =
        viewModel.incidents.collectAsState().value

    val detectedPattern =
        viewModel.detectedPattern.collectAsState().value

    val streakResult =
        viewModel.streakResult.collectAsState().value

    val total = incidents.size

    val resisted =
        incidents.count { it.wasResisted }

    val setbacks =
        incidents.count { !it.wasResisted }

    val resistanceRate =
        if (total > 0) {
            (resisted * 100) / total
        } else {
            0
        }

    val averageUrge =
        if (total > 0) {
            incidents.sumOf { it.urgeLevel }.toDouble() / total
        } else {
            0.0
        }

    val triggerCounts =
        incidents
            .groupingBy { it.trigger }
            .eachCount()
            .entries
            .sortedByDescending { it.value }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "‹",
                color = PrimaryText,
                fontSize = 34.sp,
                modifier = Modifier.clickable {
                    onBack()
                }
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {

                Text(
                    text = "INSIGHTS",
                    color = Accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Understand your recovery",
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "Your recovery,\nby the numbers.",
            color = PrimaryText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 35.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            InsightMetric(
                value = total.toString(),
                label = "Investigations",
                modifier = Modifier.weight(1f)
            )

            InsightMetric(
                value = "$resistanceRate%",
                label = "Resistance",
                modifier = Modifier.weight(1f),
                accent = true
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            InsightMetric(
                value = String.format("%.1f", averageUrge),
                label = "Average Urge",
                modifier = Modifier.weight(1f)
            )

            InsightMetric(
                value = streakResult.longestStreak.toString(),
                label = "Best Streak",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SectionTitle(
            title = "RECOVERY BALANCE",
            subtitle = "Resisted urges versus setbacks"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        BalanceCard(
            resisted = resisted,
            setbacks = setbacks,
            resistanceRate = resistanceRate
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SectionTitle(
            title = "TRIGGER PATTERNS",
            subtitle = "What appears most often"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (triggerCounts.isEmpty()) {

            EmptyCard(
                text = "Record more investigations to discover your trigger patterns."
            )

        } else {

            triggerCounts.take(5).forEach { entry ->

                TriggerCard(
                    trigger = entry.key,
                    count = entry.value,
                    total = total
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SectionTitle(
            title = "RECOVERY SIGNAL",
            subtitle = "What your evidence is telling you"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (detectedPattern == null) {

            EmptyCard(
                text = "Your pattern is still forming. Keep investigating your urges."
            )

        } else {

            PatternInsightCard(
                trigger = detectedPattern.trigger,
                habitType = detectedPattern.habitType,
                count = detectedPattern.count,
                averageUrge = detectedPattern.averageUrge
            )
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = "Insights become more useful as you record more evidence.",
            color = MutedText,
            fontSize = 11.sp,
            modifier = Modifier.padding(
                horizontal = 4.dp
            )
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
private fun InsightMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    accent: Boolean = false
) {

    Column(
        modifier = modifier
            .background(
                Surface,
                RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {

        Text(
            text = value,
            color = if (accent) Accent else PrimaryText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = MutedText,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun BalanceCard(
    resisted: Int,
    setbacks: Int,
    resistanceRate: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "RESISTED",
                    color = Success,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = resisted.toString(),
                    color = PrimaryText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = "SETBACKS",
                    color = Accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = setbacks.toString(),
                    color = PrimaryText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .background(
                    Color(0xFF292929),
                    RoundedCornerShape(10.dp)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        resistanceRate.coerceIn(0, 100) / 100f
                    )
                    .height(7.dp)
                    .background(
                        Success,
                        RoundedCornerShape(10.dp)
                    )
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "$resistanceRate% of recorded urges were resisted.",
            color = SecondaryText,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TriggerCard(
    trigger: String,
    count: Int,
    total: Int
) {

    val percentage =
        if (total > 0) {
            (count * 100) / total
        } else {
            0
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(18.dp)
            )
            .padding(17.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = trigger,
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$count ×",
                color = Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    Color(0xFF292929),
                    RoundedCornerShape(8.dp)
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        percentage.coerceIn(0, 100) / 100f
                    )
                    .height(4.dp)
                    .background(
                        Accent,
                        RoundedCornerShape(8.dp)
                    )
            )
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = "$percentage% of recorded investigations",
            color = MutedText,
            fontSize = 9.sp
        )
    }
}

@Composable
private fun PatternInsightCard(
    trigger: String,
    habitType: String,
    count: Int,
    averageUrge: Double
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF24100F),
                RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = "RECURRING TRIGGER",
            color = Accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.8.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = trigger,
            color = PrimaryText,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "$habitType • appeared $count times",
            color = SecondaryText,
            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Column {

                Text(
                    text = "AVG URGE",
                    color = MutedText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = String.format("%.1f / 10", averageUrge),
                    color = PrimaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {

                Text(
                    text = "NEXT STEP",
                    color = MutedText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Review your plan",
                    color = Accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EmptyCard(
    text: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = text,
            color = SecondaryText,
            fontSize = 12.sp
        )
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
            letterSpacing = 1.5.sp
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            color = MutedText,
            fontSize = 10.sp
        )
    }
}