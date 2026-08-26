package com.example.beingsober.ui.incident

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

private val Red = Color(0xFFFF3B30)

@Composable
fun IncidentScreen(
    habitType: String = "BOTH",
    onSaveIncident: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: IncidentViewModel = hiltViewModel()
) {

    var selectedHabit by remember {
        mutableStateOf<String?>(null)
    }

    var urgeLevel by remember {
        mutableFloatStateOf(5f)
    }

    var selectedTrigger by remember {
        mutableStateOf<String?>(null)
    }

    var selectedLocation by remember {
        mutableStateOf<String?>(null)
    }

    var notes by remember {
        mutableStateOf("")
    }
    var wasResisted by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "← BACK",
            color = Red,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
           modifier = Modifier.clickable{
               onBack()
           }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Let's investigate\nwhat happened.",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        SectionTitle("WHAT HAPPENED?")

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (habitType == "SMOKING" || habitType == "BOTH") {

                SelectionBox(
                    text = "🚬 Smoking",
                    selected = selectedHabit == "SMOKING",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedHabit = "SMOKING"
                    }
                )
            }

            if (habitType == "DRINKING" || habitType == "BOTH") {

                SelectionBox(
                    text = "🍺 Drinking",
                    selected = selectedHabit == "DRINKING",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedHabit = "DRINKING"
                    }
                )
            }
        }

        if (habitType == "BOTH") {

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            SelectionBox(
                text = "🚬🍺 Both",
                selected = selectedHabit == "BOTH",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    selectedHabit = "BOTH"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        SectionTitle("HOW STRONG WAS THE URGE?")

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "${urgeLevel.toInt()} / 10",
            color = Red,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Slider(
            value = urgeLevel,
            onValueChange = {
                urgeLevel = it
            },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        SelectionBox(
            text = if (wasResisted) {
                "✓ I RESISTED THE URGE"
            } else {
                "I DID NOT RESIST"
            },
            selected = wasResisted,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                wasResisted = !wasResisted
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        SectionTitle("WHAT WAS THE TRIGGER?")

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        val triggers = listOf(
            "Stress",
            "Boredom",
            "Anger",
            "Loneliness",
            "Social",
            "Work",
            "Celebration"
        )

        triggers.chunked(2).forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowItems.forEach { trigger ->

                    SelectionBox(
                        text = trigger,
                        selected = selectedTrigger == trigger,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedTrigger = trigger
                        }
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SectionTitle("WHERE WERE YOU?")

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        val locations = listOf(
            "Home",
            "Work",
            "Outside",
            "Bar / Restaurant",
            "With friends"
        )

        locations.chunked(2).forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowItems.forEach { location ->

                    SelectionBox(
                        text = location,
                        selected = selectedLocation == location,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedLocation = location
                        }
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        SectionTitle("WHAT HAPPENED BEFORE IT?")

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        TextField(
            value = notes,
            onValueChange = {
                notes = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = {
                Text(
                    text = "Write anything you remember...",
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0A0A0A),
                unfocusedContainerColor = Color(0xFF0A0A0A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Red,
                unfocusedIndicatorColor = Color(0xFF2A2A2A),
                cursorColor = Red
            )
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Red,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    if (
                        selectedHabit != null &&
                        selectedTrigger != null &&
                        selectedLocation != null
                    ) {

                        viewModel.saveIncident(
                            habitType = selectedHabit!!,
                            urgeLevel = urgeLevel.toInt(),
                            trigger = selectedTrigger!!,
                            location = selectedLocation!!,
                            notes = notes,
                            wasResisted = wasResisted
                        )

                        onSaveIncident()
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "SAVE CASE",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun SectionTitle(
    text: String
) {

    Text(
        text = text,
        color = Red,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp
    )
}

@Composable
private fun SelectionBox(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .height(52.dp)
            .border(
                width = 1.dp,
                color = if (selected) Red else Color(0xFF2A2A2A),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = if (selected) Red else Color.White,
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IncidentScreenPreview() {

    IncidentScreen(
        habitType = "BOTH"
    )
}