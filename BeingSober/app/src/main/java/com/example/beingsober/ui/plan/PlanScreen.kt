package com.example.beingsober.ui.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beingsober.domain.plan.RecoveryPlan

@Composable
fun PlanScreen(
    onBack: () -> Unit,
    plan: RecoveryPlan
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                color = Color(0xFFFF3B30),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onBack()
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
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
                text = "YOUR PLAN",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "A plan for the moment the urge appears.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = plan.title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Trigger: ${plan.trigger}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Habit: ${plan.habitType}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Seen ${plan.incidentCount} times  •  Average urge ${
                    String.format("%.1f", plan.averageUrge)
                }/10",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            plan.steps.forEach { step ->

                PlanCard(
                    title = "${step.number.toString().padStart(2, '0')}  ${step.title}",
                    description = step.description
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    description: String
) {

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
            text = title,
            color = Color(0xFFFF3B30),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = description,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}