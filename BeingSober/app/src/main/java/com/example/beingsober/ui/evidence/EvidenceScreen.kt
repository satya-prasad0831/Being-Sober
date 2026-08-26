package com.example.beingsober.ui.evidence

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun EvidenceScreen(
    incidents: List<IncidentEntity>,
    onBack: () -> Unit
) {

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
                    text = "EVIDENCE FILE",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Every incident you've recorded.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )
            }

            if (incidents.isEmpty()) {

                item {

                    Text(
                        text = "🔎 No evidence yet",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Your recorded incidents will appear here.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

            } else {

                items(incidents) { incident ->

                    EvidenceCard(
                        incident = incident
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
private fun EvidenceCard(
    incident: IncidentEntity
) {

    val statusText =
        if (incident.wasResisted) {
            "✓ RESISTED"
        } else {
            "⚠ SETBACK"
        }

    val statusColor =
        if (incident.wasResisted) {
            Color(0xFF4CAF50)
        } else {
            Color(0xFFFF3B30)
        }

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
            text = "🔴 ${incident.habitType}",
            color = Color(0xFFFF3B30),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = statusText,
            color = statusColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Trigger: ${incident.trigger}",
            color = Color.White,
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Location: ${incident.location}",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Urge: ${incident.urgeLevel}/10",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = formatIncidentDate(incident.timestamp),
            color = Color.Gray,
            fontSize = 12.sp
        )

        if (incident.notes.isNotBlank()) {

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Notes: ${incident.notes}",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

private fun formatIncidentDate(
    timestamp: Long
): String {

    val formatter =
        java.text.SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            java.util.Locale.getDefault()
        )

    return formatter.format(
        java.util.Date(timestamp)
    )
}