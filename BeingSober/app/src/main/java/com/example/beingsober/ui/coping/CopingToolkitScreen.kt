package com.example.beingsober.ui.coping

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Background = Color(0xFF070707)
private val Surface = Color(0xFF121212)
private val SurfaceElevated = Color(0xFF191919)
private val Accent = Color(0xFFFF3B30)
private val Calm = Color(0xFF69D391)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF929292)
private val MutedText = Color(0xFF656565)

@Composable
fun CopingToolkitScreen(
    onBack: () -> Unit,
    onCalmDrop: () -> Unit
) {

    var activeTool by rememberSaveable {
        mutableStateOf("menu")
    }

    when (activeTool) {

        "breathing" -> {

            BreathingReset(
                onBack = {
                    activeTool = "menu"
                }
            )
        }

        "write" -> {

            WriteItOut(
                onBack = {
                    activeTool = "menu"
                }
            )
        }

        "environment" -> {

            ChangeEnvironment(
                onBack = {
                    activeTool = "menu"
                }
            )
        }

        else -> {

            ToolkitMenu(
                onBack = onBack,
                onCalmDrop = onCalmDrop,
                onBreathing = {
                    activeTool = "breathing"
                },
                onWrite = {
                    activeTool = "write"
                },
                onEnvironment = {
                    activeTool = "environment"
                }
            )
        }
    }
}

@Composable
private fun ToolkitMenu(
    onBack: () -> Unit,
    onCalmDrop: () -> Unit,
    onBreathing: () -> Unit,
    onWrite: () -> Unit,
    onEnvironment: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )
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
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "COPING TOOLKIT",
                color = Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "I'm having an urge.",
                color = PrimaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "What would help right now?",
                color = SecondaryText,
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            ToolkitCard(
                icon = "◎",
                title = "Calm Drop",
                subtitle = "Focus on the moment for 30 seconds.",
                tag = "INTERACTIVE",
                accent = Calm,
                onClick = onCalmDrop
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ToolkitCard(
                icon = "◌",
                title = "Breathing Reset",
                subtitle = "Slow your breathing and regain control.",
                tag = "1 MINUTE",
                accent = Calm,
                onClick = onBreathing
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ToolkitCard(
                icon = "✎",
                title = "Write It Out",
                subtitle = "Put the feeling into words instead of acting on it.",
                tag = "REFLECT",
                accent = Accent,
                onClick = onWrite
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ToolkitCard(
                icon = "→",
                title = "Change Environment",
                subtitle = "Break the situation that is feeding the urge.",
                tag = "TAKE ACTION",
                accent = Accent,
                onClick = onEnvironment
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Surface,
                        RoundedCornerShape(22.dp)
                    )
                    .padding(20.dp)
            ) {

                Text(
                    text = "REMEMBER",
                    color = MutedText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "An urge is a moment, not a command.",
                    color = PrimaryText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Give yourself a little time before making the next choice.",
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun BreathingReset(
    onBack: () -> Unit
) {

    var running by remember {
        mutableStateOf(false)
    }

    var seconds by remember {
        mutableStateOf(60)
    }

    var phase by remember {
        mutableStateOf("READY")
    }

    LaunchedEffect(running) {

        if (running) {

            while (seconds > 0) {

                phase = when ((60 - seconds) % 14) {
                    in 0..3 -> "INHALE"
                    in 4..7 -> "HOLD"
                    else -> "EXHALE"
                }

                delay(1000)
                seconds--
            }

            phase = "COMPLETE"
            running = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "‹",
            color = PrimaryText,
            fontSize = 34.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onBack()
                }
        )

        Spacer(
            modifier = Modifier.height(35.dp)
        )

        Text(
            text = "BREATHING RESET",
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    Calm.copy(alpha = 0.12f)
                )
                .padding(55.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (running) {
                    seconds.toString()
                } else {
                    "60"
                },
                color = Calm,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = phase,
            color = PrimaryText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = when (phase) {
                "INHALE" -> "Slowly breathe in"
                "HOLD" -> "Hold your breath"
                "EXHALE" -> "Slowly breathe out"
                "COMPLETE" -> "You completed the reset"
                else -> "Give yourself one minute"
            },
            color = SecondaryText,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                seconds = 60
                running = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent
            )
        ) {

            Text(
                text = if (phase == "COMPLETE") {
                    "START AGAIN"
                } else {
                    "START RESET"
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun WriteItOut(
    onBack: () -> Unit
) {

    var text by remember {
        mutableStateOf("")
    }

    var saved by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
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
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "WRITE IT OUT",
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "What's happening\nright now?",
            color = PrimaryText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Put the thought somewhere other than your head.",
            color = SecondaryText,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                saved = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            placeholder = {
                Text(
                    text = "I am feeling...",
                    color = MutedText
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Accent,
                unfocusedBorderColor = SurfaceElevated,
                focusedTextColor = PrimaryText,
                unfocusedTextColor = PrimaryText,
                cursorColor = Accent
            ),
            shape = RoundedCornerShape(20.dp)
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        if (saved) {

            Text(
                text = "✓ Thought saved for this session.",
                color = Calm,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                saved = true
            },
            enabled = text.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent,
                disabledContainerColor = SurfaceElevated
            )
        ) {

            Text(
                text = "SAVE THOUGHT",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ChangeEnvironment(
    onBack: () -> Unit
) {

    var selected by remember {
        mutableStateOf<String?>(null)
    }

    var completed by remember {
        mutableStateOf(false)
    }

    val actions = listOf(
        "Take a 5-minute walk",
        "Move to another room",
        "Get some water",
        "Call someone you trust"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
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
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "CHANGE ENVIRONMENT",
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Break the pattern.",
            color = PrimaryText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Changing what surrounds you can interrupt an automatic response.",
            color = SecondaryText,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        actions.forEach { action ->

            val isSelected =
                selected == action

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) {
                            Color(0xFF24100F)
                        } else {
                            Surface
                        },
                        RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        selected = action
                        completed = false
                    }
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (isSelected) "●" else "○",
                    color = if (isSelected) {
                        Accent
                    } else {
                        MutedText
                    },
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Text(
                    text = action,
                    color = if (isSelected) {
                        PrimaryText
                    } else {
                        SecondaryText
                    },
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        if (completed) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF102018),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "✓",
                    color = Calm,
                    fontSize = 30.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "ENVIRONMENT CHANGED",
                    color = Calm,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Give the urge a few minutes to settle.",
                    color = SecondaryText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }

        Button(
            onClick = {
                completed = true
            },
            enabled = selected != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent,
                disabledContainerColor = SurfaceElevated
            )
        ) {

            Text(
                text = "I'VE CHANGED MY ENVIRONMENT",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ToolkitCard(
    icon: String,
    title: String,
    subtitle: String,
    tag: String,
    accent: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    accent.copy(alpha = 0.12f)
                )
                .padding(13.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = icon,
                color = accent,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(15.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    color = PrimaryText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = tag,
                    color = accent,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = subtitle,
                color = SecondaryText,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = "→",
            color = MutedText,
            fontSize = 19.sp
        )
    }
}