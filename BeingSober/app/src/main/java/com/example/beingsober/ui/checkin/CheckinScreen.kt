package com.example.beingsober.ui.checkin

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
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

private val Background = Color(0xFF050505)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)
private val BorderColor = Color(0xFF262626)

@Composable
fun CheckInScreen(
    onBack: () -> Unit
) {

    var mood by remember {
        mutableFloatStateOf(5f)
    }

    var stress by remember {
        mutableFloatStateOf(5f)
    }

    var urge by remember {
        mutableFloatStateOf(5f)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
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
                text = "← BACK",
                color = Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onBack()
                }
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

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
                text = "DAILY CHECK-IN",
                color = PrimaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Take a moment to understand where you are today.",
                color = SecondaryText,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            CheckInCard(
                title = "MOOD",
                value = mood,
                onValueChange = {
                    mood = it
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            CheckInCard(
                title = "STRESS",
                value = stress,
                onValueChange = {
                    stress = it
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            CheckInCard(
                title = "URGE",
                value = urge,
                onValueChange = {
                    urge = it
                }
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Accent,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Check-in complete"
                            )
                        }
                    }
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "SAVE CHECK-IN",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun CheckInCard(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                BorderColor,
                RoundedCornerShape(16.dp)
            )
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                color = PrimaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "${value.toInt()}/10",
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..10f,
            steps = 8
        )
    }
}