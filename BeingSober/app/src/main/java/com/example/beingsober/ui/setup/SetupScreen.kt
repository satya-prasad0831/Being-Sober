package com.example.beingsober.ui.setup

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetupScreen(
    onSetupComplete: (String) -> Unit
) {

    var selectedHabit by remember {
        mutableStateOf<String?>(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "BEING SOBER",
                color = Color(0xFFFF3B30),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = "What are you\nworking on?",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Choose what you want to understand\nand change.",
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            HabitOption(
                title = "🚬  Smoking",
                selected = selectedHabit == "SMOKING",
                onClick = {
                    selectedHabit = "SMOKING"
                }
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            HabitOption(
                title = "🍺  Drinking",
                selected = selectedHabit == "DRINKING",
                onClick = {
                    selectedHabit = "DRINKING"
                }
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            HabitOption(
                title = "🚬🍺  Both",
                selected = selectedHabit == "BOTH",
                onClick = {
                    selectedHabit = "BOTH"
                }
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Button(
                onClick = {
                    selectedHabit?.let {
                        onSetupComplete(it)
                    }
                },
                enabled = selectedHabit != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF3B30),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF2A2A2A),
                    disabledContentColor = Color.Gray
                )
            ) {

                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HabitOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val borderColor =
        if (selected) {
            Color(0xFFFF3B30)
        } else {
            Color(0xFF2A2A2A)
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = title,
            color = if (selected) {
                Color(0xFFFF3B30)
            } else {
                Color.White
            },
            fontSize = 17.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}