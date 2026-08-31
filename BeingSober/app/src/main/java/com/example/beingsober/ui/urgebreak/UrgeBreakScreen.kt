package com.example.beingsober.ui.urgebreak

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

private val Background = Color(0xFF050505)
private val Surface = Color(0xFF111111)
private val Accent = Color(0xFFFF3B30)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF8E8E93)
private val Calm = Color(0xFF6FE7C8)

private data class Target(
    val id: Int,
    val x: Float,
    val y: Float,
    val radius: Float
)

@Composable
fun UrgeBreakScreen(
    onBack: () -> Unit
) {

    var started by remember {
        mutableStateOf(false)
    }

    var finished by remember {
        mutableStateOf(false)
    }

    var timeLeft by remember {
        mutableIntStateOf(30)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var nextId by remember {
        mutableIntStateOf(0)
    }

    var feedback by remember {
        mutableStateOf("")
    }

    val targets = remember {
        mutableStateListOf<Target>()
    }

    fun addTarget() {
        nextId++

        targets.add(
            Target(
                id = nextId,
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextInt(32, 48).toFloat()
            )
        )
    }

    fun startGame() {
        started = true
        finished = false
        timeLeft = 30
        score = 0
        feedback = ""
        targets.clear()

        repeat(6) {
            addTarget()
        }
    }

    LaunchedEffect(started) {

        if (started && !finished) {

            while (timeLeft > 0) {

                delay(1000)

                timeLeft--
            }

            finished = true
            started = false
            targets.clear()
        }
    }

    if (finished) {

        ResultScreen(
            score = score,
            onBack = onBack,
            onAgain = {
                startGame()
            }
        )

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
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "URGE BREAK",
                color = PrimaryText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "A short pause can change the next moment.",
                color = SecondaryText,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            if (!started) {

                StartScreen(
                    onStart = {
                        startGame()
                    }
                )

            } else {

                GameHeader(
                    timeLeft = timeLeft,
                    score = score
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            Surface,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(12.dp)
                ) {

                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        targets.forEach { target ->

                            val center = Offset(
                                x = target.x * size.width,
                                y = target.y * size.height
                            )

                            drawTarget(
                                center = center,
                                radius = target.radius
                            )
                        }
                    }

                    targets.forEach { target ->

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {

                                    val removed =
                                        targets.removeAll {
                                            it.id == target.id
                                        }

                                    if (removed) {

                                        score++

                                        feedback = "NICE"

                                        addTarget()
                                    }
                                }
                        )
                    }

                    if (feedback.isNotEmpty()) {

                        Text(
                            text = feedback,
                            color = Calm,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 14.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Tap the glowing circles and stay with the moment.",
                    color = SecondaryText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun DrawScope.drawTarget(
    center: Offset,
    radius: Float
) {

    drawCircle(
        color = Calm.copy(alpha = 0.15f),
        radius = radius * 1.8f,
        center = center
    )

    drawCircle(
        color = Calm.copy(alpha = 0.35f),
        radius = radius * 1.3f,
        center = center
    )

    drawCircle(
        color = Calm,
        radius = radius,
        center = center
    )

    drawCircle(
        color = Color.White.copy(alpha = 0.8f),
        radius = radius * 0.28f,
        center = center
    )
}

@Composable
private fun GameHeader(
    timeLeft: Int,
    score: Int
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text = "TIME",
                color = SecondaryText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "${timeLeft}s",
                color = PrimaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = "SCORE",
                color = SecondaryText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Text(
                text = score.toString(),
                color = Calm,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StartScreen(
    onStart: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "CALM DROP",
            color = PrimaryText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Tap the glowing circles.\nKeep your attention here for 30 seconds.",
            color = SecondaryText,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Box(
            modifier = Modifier
                .background(
                    Accent,
                    RoundedCornerShape(16.dp)
                )
                .clickable {
                    onStart()
                }
                .padding(
                    horizontal = 36.dp,
                    vertical = 17.dp
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "START",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }
    }
}

@Composable
private fun ResultScreen(
    score: Int,
    onBack: () -> Unit,
    onAgain: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "✦",
                color = Calm,
                fontSize = 48.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "PAUSE COMPLETE",
                color = PrimaryText,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "You created $score moments of focus.",
                color = SecondaryText,
                fontSize = 15.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Take a breath and notice how you feel now.",
                color = SecondaryText,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Accent,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onAgain()
                    }
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "PLAY AGAIN",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "← BACK TO RECOVERY",
                color = Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onBack()
                }
            )
        }
    }
}