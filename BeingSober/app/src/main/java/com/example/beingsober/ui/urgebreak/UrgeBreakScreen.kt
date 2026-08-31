package com.example.beingsober.ui.urgebreak

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

private val Background = Color(0xFF050505)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)

@Composable
fun UrgeBreakScreen(
    onBack: () -> Unit
) {

    var gameStarted by remember {
        mutableStateOf(false)
    }

    var gameFinished by remember {
        mutableStateOf(false)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var timeLeft by remember {
        mutableIntStateOf(30)
    }

    var targetX by remember {
        mutableIntStateOf(0)
    }

    var targetY by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(gameStarted) {

        if (gameStarted) {

            while (timeLeft > 0) {

                delay(1000)

                timeLeft--
            }

            gameFinished = true
        }
    }

    fun moveTarget() {

        targetX =
            Random.nextInt(
                -120,
                121
            )

        targetY =
            Random.nextInt(
                -220,
                221
            )
    }

    if (gameFinished) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,

                modifier =
                    Modifier.padding(24.dp)
            ) {

                Text(
                    text = "🔥",
                    fontSize = 48.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text = "YOU CREATED A PAUSE",
                    color = PrimaryText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text = "You tapped $score times.",
                    color = SecondaryText,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text =
                        "Take a moment and notice how the urge feels now.",
                    color = SecondaryText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(28.dp)
                )

                Text(
                    text = "← BACK",
                    color = Accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier.clickable {
                            onBack()
                        }
                )
            }
        }

        return
    }

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
                modifier =
                    Modifier.height(20.dp)
            )

            Text(
                text = "← BACK",
                color = Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier.clickable {
                        onBack()
                    }
            )

            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )

            Text(
                text = "BEING SOBER",
                color = Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text = "URGE BREAK",
                color = PrimaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    if (gameStarted)
                        "Stay with the moment. Keep tapping."
                    else
                        "Give your mind something else to focus on.",
                color = SecondaryText,
                fontSize = 14.sp
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            if (!gameStarted) {

                Column(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "CATCH THE CALM",
                        color = PrimaryText,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            "Tap the circle whenever it appears.\n" +
                                    "You have 30 seconds.",
                        color = SecondaryText,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier =
                            Modifier.height(30.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Accent)
                            .clickable {

                                gameStarted = true
                                timeLeft = 30
                                score = 0

                                moveTarget()
                            }
                            .padding(
                                horizontal = 32.dp,
                                vertical = 18.dp
                            )
                    ) {

                        Text(
                            text = "START",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            } else {

                RowHeader(
                    timeLeft = timeLeft,
                    score = score
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    targetX,
                                    targetY
                                )
                            }
                            .clip(CircleShape)
                            .background(Accent)
                            .clickable {

                                score++

                                moveTarget()
                            }
                            .padding(22.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = "●",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }

                Text(
                    text =
                        "Focus on the next tap.",
                    color = SecondaryText,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun RowHeader(
    timeLeft: Int,
    score: Int
) {

    androidx.compose.foundation.layout.Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = "TIME  ${timeLeft}s",
            color = PrimaryText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "TAPS  $score",
            color = Accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}