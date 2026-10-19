package com.example.beingsober.ui.challenge

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

private val Background = Color(0xFF070707)
private val Surface = Color(0xFF121212)
private val SurfaceElevated = Color(0xFF191919)
private val Accent = Color(0xFFFF3B30)
private val Success = Color(0xFF69D391)
private val PrimaryText = Color.White
private val SecondaryText = Color(0xFF929292)
private val MutedText = Color(0xFF606060)

@Composable
fun ChallengeScreen(
    onBack: () -> Unit,
    viewModel: ChallengeViewModel = hiltViewModel()
) {

    val challenge by viewModel.latestChallenge.collectAsState()

    var creatingNewChallenge by remember {
        mutableStateOf(false)
    }

    if (creatingNewChallenge || challenge == null) {

        ChallengeSetupScreen(
            onBack = {
                if (creatingNewChallenge && challenge != null) {
                    creatingNewChallenge = false
                } else {
                    onBack()
                }
            },
            onStart = { goalName, goalAmount, costPerUrge ->

                viewModel.createChallenge(
                    goalName = goalName,
                    goalAmount = goalAmount,
                    costPerUrge = costPerUrge
                )

                creatingNewChallenge = false
            }
        )

    } else {

        ChallengeDashboard(
            challenge = challenge!!,
            onBack = onBack,
            onResisted = {
                viewModel.recordResistance()
            },
            onNewChallenge = {
                creatingNewChallenge = true
            },
            onDelete = {
                viewModel.deleteChallenge()
            }
        )
    }
}

@Composable
private fun ChallengeSetupScreen(
    onBack: () -> Unit,
    onStart: (
        String,
        Double,
        Double
    ) -> Unit
) {

    var goalName by remember {
        mutableStateOf("")
    }

    var goalAmount by remember {
        mutableStateOf("")
    }

    var costPerUrge by remember {
        mutableStateOf("")
    }

    val valid =
        goalName.isNotBlank() &&
                (goalAmount.toDoubleOrNull() ?: 0.0) > 0 &&
                (costPerUrge.toDoubleOrNull() ?: 0.0) > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
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
            text = "BEING SOBER",
            color = Accent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.5.sp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "THE\nCHALLENGE",
            color = PrimaryText,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Turn resistance into something real.",
            color = SecondaryText,
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "WHAT ARE YOU SAVING FOR?",
            color = Accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        ChallengeField(
            value = goalName,
            placeholder = "New phone",
            onValueChange = {
                goalName = it
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "GOAL AMOUNT",
            color = Accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        ChallengeField(
            value = goalAmount,
            placeholder = "35000",
            prefix = "₹",
            onValueChange = {
                goalAmount = it
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "COST PER URGE",
            color = Accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(
            modifier = Modifier.height(9.dp)
        )

        ChallengeField(
            value = costPerUrge,
            placeholder = "180",
            prefix = "₹",
            onValueChange = {
                costPerUrge = it
            }
        )

        Spacer(
            modifier = Modifier.height(26.dp)
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
                text = "THE IDEA",
                color = MutedText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(
                modifier = Modifier.height(9.dp)
            )

            Text(
                text = "Every time you resist, the money you might have spent becomes potential progress toward something you actually want.",
                color = PrimaryText,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Button(
            onClick = {
                onStart(
                    goalName,
                    goalAmount.toDouble(),
                    costPerUrge.toDouble()
                )
            },
            enabled = valid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent,
                disabledContainerColor = SurfaceElevated,
                disabledContentColor = MutedText
            )
        ) {

            Text(
                text = "START MY CHALLENGE  →",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ChallengeField(
    value: String,
    placeholder: String,
    prefix: String? = null,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = MutedText
            )
        },
        prefix = prefix?.let {
            {
                Text(
                    text = it,
                    color = PrimaryText,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Accent,
            unfocusedBorderColor = Color(0xFF292929),
            focusedTextColor = PrimaryText,
            unfocusedTextColor = PrimaryText,
            cursorColor = Accent
        )
    )
}

@Composable
private fun ChallengeDashboard(
    challenge: com.example.beingsober.data.local.entity.ChallengeEntity,
    onBack: () -> Unit,
    onResisted: () -> Unit,
    onNewChallenge: () -> Unit,
    onDelete: () -> Unit
) {

    val savings =
        challenge.resistedCount *
                challenge.costPerUrge

    val progress =
        if (challenge.goalAmount > 0) {
            (savings / challenge.goalAmount)
                .coerceIn(0.0, 1.0)
                .toFloat()
        } else {
            0f
        }

    val percentage =
        (progress * 100).toInt()

    val completed =
        !challenge.isActive

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "‹",
                color = PrimaryText,
                fontSize = 34.sp,
                modifier = Modifier.clickable {
                    onBack()
                }
            )

            Text(
                text = "CHALLENGE",
                color = SecondaryText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Text(
            text = if (completed) {
                "CHALLENGE COMPLETED"
            } else {
                "YOUR CHALLENGE"
            },
            color = if (completed) Success else Accent,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = challenge.goalName,
            color = PrimaryText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        if (completed) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF102018),
                        RoundedCornerShape(26.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            Success.copy(alpha = 0.12f)
                        )
                        .padding(18.dp)
                ) {

                    Text(
                        text = "✓",
                        color = Success,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "GOAL REACHED",
                    color = Success,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = "You turned resistance into ₹${"%,.0f".format(savings)} of potential savings.",
                    color = SecondaryText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Surface,
                        RoundedCornerShape(26.dp)
                    )
                    .padding(22.dp)
            ) {

                Text(
                    text = "POTENTIAL SAVINGS",
                    color = SecondaryText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = "₹${"%,.0f".format(savings)}",
                    color = Success,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "of ₹${"%,.0f".format(challenge.goalAmount)}",
                    color = SecondaryText,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            Color(0xFF292929),
                            RoundedCornerShape(8.dp)
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .background(
                                Success,
                                RoundedCornerShape(8.dp)
                            )
                    )
                }

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                Text(
                    text = "$percentage% COMPLETE",
                    color = Success,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            ChallengeStat(
                value = challenge.resistedCount.toString(),
                label = "URGES\nRESISTED",
                modifier = Modifier.weight(1f)
            )

            ChallengeStat(
                value = "₹${"%,.0f".format(challenge.costPerUrge)}",
                label = "VALUE /\nRESISTANCE",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        if (!completed) {

            Text(
                text = "READY FOR THE NEXT ONE?",
                color = Accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "Every resisted urge moves you closer to your goal.",
                color = SecondaryText,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onResisted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent
                )
            ) {

                Text(
                    text = "🔥  I RESISTED AN URGE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        } else {

            Text(
                text = "WHAT'S NEXT?",
                color = Accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "This challenge is complete. Start another goal or remove this one.",
                color = SecondaryText,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onNewChallenge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent
                )
            ) {

                Text(
                    text = "＋  NEW CHALLENGE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceElevated,
                contentColor = SecondaryText
            )
        ) {

            Text(
                text = "DELETE CHALLENGE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
private fun ChallengeStat(
    value: String,
    label: String,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .background(
                Surface,
                RoundedCornerShape(20.dp)
            )
            .padding(17.dp)
    ) {

        Text(
            text = value,
            color = PrimaryText,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = label,
            color = MutedText,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 13.sp
        )
    }
}