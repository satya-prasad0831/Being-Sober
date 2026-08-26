package com.example.beingsober

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.beingsober.data.local.PreferencesManager
import com.example.beingsober.data.soberQuotes
import com.example.beingsober.ui.home.HomeScreen
import com.example.beingsober.ui.setup.SetupScreen
import com.example.beingsober.ui.theme.BeingSoberTheme
import kotlinx.coroutines.delay
import dagger.hilt.android.AndroidEntryPoint
import com.example.beingsober.ui.incident.IncidentScreen
import com.example.beingsober.ui.patterns.PatternsScreen
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.beingsober.ui.incident.IncidentViewModel
import com.example.beingsober.ui.evidence.EvidenceScreen
import com.example.beingsober.ui.plan.PlanScreen
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val preferencesManager = PreferencesManager(this)

        setContent {

            BeingSoberTheme {
                val incidentViewModel: IncidentViewModel = hiltViewModel()

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
                var showPatterns by remember {
                    mutableStateOf(false)
                }
                var showEvidence by remember {
                    mutableStateOf(false)
                }
                var showPlan by remember {
                    mutableStateOf(false)
                }
                val incidents =
                    incidentViewModel.incidents.collectAsState().value

                val recoveryPlan =
                    incidentViewModel.recoveryPlan.collectAsState().value

                BackHandler(
                    enabled = showIncident || showPatterns || showEvidence || showPlan
                ) {
                    when {
                        showIncident -> showIncident = false
                        showPatterns -> showPatterns = false
                        showEvidence -> showEvidence = false
                        showPlan -> showPlan = false
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

                }  else if (showIncident) {

                    IncidentScreen(
                        habitType = preferencesManager.getHabitType() ?: "BOTH",

                        onSaveIncident = {
                            showIncident = false
                        },
                        onBack = {
                            showIncident = false
                        }
                    )

                } else if (showPatterns) {

                    PatternsScreen(
                        incidents = incidents,
                        onBack = {
                            showPatterns = false
                        }
                    )

                } else if (showEvidence) {

                    EvidenceScreen(
                        incidents = incidents,
                        onBack = {
                            showEvidence = false
                        }
                    )

                }  else if (showPlan) {

            PlanScreen(
                plan = recoveryPlan,
                onBack = {
                    showPlan = false
                }
            )

                } else {

                    HomeScreen(
                        habitType = preferencesManager.getHabitType(),

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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    val quote = remember {
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
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "\"$quote\"",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "— BEING SOBER",
                color = Color(0xFFFF3B30),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}