package com.example.beingsober

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.data.local.PreferencesManager
import com.example.beingsober.data.soberQuotes
import com.example.beingsober.ui.checkin.CheckInScreen
import com.example.beingsober.ui.challenge.ChallengeScreen
import com.example.beingsober.ui.coping.CopingToolkitScreen
import com.example.beingsober.ui.evidence.EvidenceScreen
import com.example.beingsober.ui.home.HomeScreen
import com.example.beingsober.ui.incident.IncidentScreen
import com.example.beingsober.ui.incident.IncidentViewModel
import com.example.beingsober.ui.insights.InsightsScreen
import com.example.beingsober.ui.patterns.PatternsScreen
import com.example.beingsober.ui.plan.PlanScreen
import com.example.beingsober.ui.setup.SetupScreen
import com.example.beingsober.ui.statistics.StatisticsScreen
import com.example.beingsober.ui.theme.BeingSoberTheme
import com.example.beingsober.ui.urgebreak.UrgeBreakScreen
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Log.e(
                        "FCM_TOKEN",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@addOnCompleteListener
                }

                Log.d(
                    "FCM_TOKEN",
                    "FCM Token: ${task.result}"
                )
            }

        enableEdgeToEdge()

        val preferencesManager =
            PreferencesManager(this)

        setContent {

            BeingSoberTheme {

                val incidentViewModel: IncidentViewModel =
                    hiltViewModel()

                var showSplash by remember {
                    mutableStateOf(true)
                }

                var setupComplete by remember {
                    mutableStateOf(
                        preferencesManager.isSetupComplete()
                    )
                }

                var showIncident by remember {
                    mutableStateOf(false)
                }

                var showInsights by remember {
                    mutableStateOf(false)
                }

                var showPatterns by remember {
                    mutableStateOf(false)
                }

                var showEvidence by remember {
                    mutableStateOf(false)
                }

                var showPlan by remember {
                    mutableStateOf(false)
                }

                var showStatistics by remember {
                    mutableStateOf(false)
                }

                var showUrgeBreak by remember {
                    mutableStateOf(false)
                }

                var showCheckIn by remember {
                    mutableStateOf(false)
                }
                var showChallenge by remember {
                    mutableStateOf(false)
                }

                var showCopingToolkit by remember {
                    mutableStateOf(false)
                }

                val incidents =
                    incidentViewModel
                        .incidents
                        .collectAsState()
                        .value

                val recoveryPlan =
                    incidentViewModel
                        .recoveryPlan
                        .collectAsState()
                        .value

                BackHandler(
                    enabled =
                        showIncident ||
                                showInsights ||
                                showPatterns ||
                                showEvidence ||
                                showPlan ||
                                showStatistics ||
                                showUrgeBreak ||
                                showCheckIn ||
                                showChallenge ||
                                showCopingToolkit
                ) {

                    when {

                        showIncident ->
                            showIncident = false

                        showInsights ->
                            showInsights = false

                        showPatterns ->
                            showPatterns = false

                        showEvidence ->
                            showEvidence = false

                        showPlan ->
                            showPlan = false

                        showStatistics ->
                            showStatistics = false

                        showUrgeBreak ->
                            showUrgeBreak = false

                        showCheckIn ->
                            showCheckIn = false

                        showChallenge ->
                            showChallenge = false

                        showCopingToolkit ->
                            showCopingToolkit = false
                    }
                }

                if (showSplash) {

                    SplashScreen(
                        onFinished = {
                            showSplash = false
                        }
                    )

                } else if (!setupComplete) {

                    SetupScreen(
                        onSetupComplete = { habitType ->

                            preferencesManager.saveHabitType(
                                habitType
                            )

                            setupComplete = true
                        }
                    )

                } else if (showUrgeBreak) {

                    UrgeBreakScreen(
                        onBack = {
                            showUrgeBreak = false
                        }
                    )

                } else if (showCheckIn) {

                    CheckInScreen(
                        onBack = {
                            showCheckIn = false
                        }
                    )

                } else {

                    val currentScreen =
                        when {
                            showChallenge ->
                                "challenge"

                            showCopingToolkit ->
                                "coping"

                            showInsights ->
                                "insights"

                            showIncident ->
                                "incident"

                            showPatterns ->
                                "patterns"

                            showEvidence ->
                                "evidence"

                            showStatistics ->
                                "statistics"

                            showPlan ->
                                "plan"

                            else ->
                                "home"
                        }

                    AnimatedContent(
                        targetState = currentScreen,

                        transitionSpec = {

                            (
                                    slideInHorizontally(
                                        animationSpec = tween(300),
                                        initialOffsetX = { it }
                                    ) +
                                            fadeIn(
                                                animationSpec = tween(300)
                                            )
                                    ).togetherWith(

                                    slideOutHorizontally(
                                        animationSpec = tween(200),
                                        targetOffsetX = { -it / 4 }
                                    ) +
                                            fadeOut(
                                                animationSpec = tween(200)
                                            )
                                )
                        },

                        label = "BeingSoberScreenTransition"

                    ) { screen ->

                        when (screen) {
                            "challenge" -> {

                                ChallengeScreen(
                                    onBack = {
                                        showChallenge = false
                                    }
                                )
                            }

                            "coping" -> {

                                CopingToolkitScreen(
                                    onBack = {
                                        showCopingToolkit = false
                                    },
                                    onCalmDrop = {
                                        showCopingToolkit = false
                                        showUrgeBreak = true
                                    }
                                )
                            }

                            "insights" -> {

                                InsightsScreen(
                                    onBack = {
                                        showInsights = false
                                    }
                                )
                            }

                            "incident" -> {

                                IncidentScreen(

                                    habitType =
                                        preferencesManager
                                            .getHabitType()
                                            ?: "BOTH",

                                    onSaveIncident = {
                                        showIncident = false
                                    },

                                    onBack = {
                                        showIncident = false
                                    }
                                )
                            }

                            "patterns" -> {

                                PatternsScreen(

                                    incidents = incidents,

                                    onBack = {
                                        showPatterns = false
                                    }
                                )
                            }

                            "evidence" -> {

                                EvidenceScreen(

                                    incidents = incidents,

                                    onBack = {
                                        showEvidence = false
                                    }
                                )
                            }

                            "statistics" -> {

                                val streakResult =
                                    incidentViewModel
                                        .streakResult
                                        .collectAsState()
                                        .value

                                StatisticsScreen(

                                    incidents = incidents,

                                    longestStreak =
                                        streakResult.longestStreak,

                                    onBack = {
                                        showStatistics = false
                                    }
                                )
                            }

                            "plan" -> {

                                if (recoveryPlan == null) {

                                    NoPlanScreen(
                                        onBack = {
                                            showPlan = false
                                        }
                                    )

                                } else {

                                    PlanScreen(

                                        plan = recoveryPlan,

                                        onBack = {
                                            showPlan = false
                                        }
                                    )
                                }
                            }

                            else -> {

                                HomeScreen(

                                    habitType =
                                        preferencesManager
                                            .getHabitType(),

                                    onNewIncident = {
                                        showIncident = true
                                    },

                                    onPatterns = {
                                        showPatterns = true
                                    },

                                    onEvidence = {
                                        showEvidence = true
                                    },

                                    onPlan = {
                                        showPlan = true
                                    },

                                    onStatistics = {
                                        showInsights = true
                                    },

                                    onUrgeBreak = {
                                        showCopingToolkit = true
                                    },

                                    onCheckIn = {
                                        showCheckIn = true
                                    },
                                    onChallenge = {
                                        showChallenge = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        FirebaseMessaging
            .getInstance()
            .token
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Log.e(
                        "FCM",
                        "Failed to get FCM token",
                        task.exception
                    )
                    return@addOnCompleteListener
                }

                Log.d(
                    "FCM",
                    task.result
                )
            }
    }
}

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    val quote =
        remember {
            soberQuotes.random()
        }

    LaunchedEffect(Unit) {

        delay(3000)

        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = "\"$quote\"",

                color = Color.White,

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.Medium,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Text(
                text = "— BEING SOBER",

                color =
                    Color(0xFFFF3B30),

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun NoPlanScreen(
    onBack: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "← BACK",

                color =
                    Color(0xFFFF3B30),

                fontSize = 13.sp,

                fontWeight =
                    FontWeight.Bold,

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
                text = "🔎",

                fontSize = 40.sp
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text = "NO PATTERN YET",

                color =
                    Color.White,

                fontSize = 22.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "Record at least two similar incidents\n" +
                            "before we create a personalized plan.",

                color =
                    Color.Gray,

                fontSize = 14.sp,

                textAlign =
                    TextAlign.Center
            )
        }
    }
}