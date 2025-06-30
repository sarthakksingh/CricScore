package com.example.cricscore.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cricscore.R
import com.example.cricscore.view.components.CollapsibleHeader
import com.example.cricscore.view.components.OverRow
import com.example.cricscore.view.components.PlayerRow
import com.example.cricscore.view.components.ScoreCard
import com.example.cricscore.view.components.TeamToggleTile
import com.example.cricscore.viewModel.MatchPhase
import com.example.cricscore.viewModel.MatchViewModel

@Composable
fun MatchSummaryScreen(
    navController: NavController,
    matchViewModel: MatchViewModel = viewModel()
) {
    val inningsA = matchViewModel.inningsA
    val inningsB = matchViewModel.inningsB
    val phase = matchViewModel.phase
    val target = matchViewModel.targetScore

    val backgroundRes: Int = R.drawable.summary_banner
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var showPlayers by rememberSaveable { mutableStateOf(true) }
    var showOvers by rememberSaveable { mutableStateOf(false) }

    val current = if (selected == 0) inningsA else inningsB

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.55f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(32.dp))
            Text("Match Summary", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                TeamToggleTile("Team A", selected == 0, Modifier.weight(1f)) { selected = 0 }
                TeamToggleTile("Team B", selected == 1, Modifier.weight(1f)) { selected = 1 }
            }

            Spacer(Modifier.height(14.dp))

            if (current == null) {
                Text("No data available", fontSize = 18.sp, color = Color.Red)
            } else {
                ScoreCard(current)
                Spacer(Modifier.height(12.dp))

                if (selected == 1 && phase != MatchPhase.FIRST && inningsA != null) {
                    Text(
                        "Target: $target runs",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(Modifier.height(8.dp))

                    if (inningsB != null && inningsB.completed) {
                        val result = when {
                            inningsB.totalRuns > inningsA.totalRuns ->
                                "${inningsB.teamName} won by ${10 - inningsB.totalWkts} wickets"
                            inningsB.totalRuns < inningsA.totalRuns ->
                                "${inningsA.teamName} won by ${inningsA.totalRuns - inningsB.totalRuns} runs"
                            else -> "Match Drawn"
                        }
                        Text(result, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2E7D32))
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (!current.completed) {
                    Text("Haven’t batted yet.", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Red)
                } else {
                    CollapsibleHeader("Player Stats", showPlayers) { showPlayers = !showPlayers }
                    if (showPlayers) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.85f))
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                current.players.forEach { PlayerRow(it) }
                            }
                        }
                    }

                    CollapsibleHeader("Over‑wise Scores", showOvers) { showOvers = !showOvers }
                    if (showOvers) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.85f))
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                current.overs.forEachIndexed { index, over ->
                                    OverRow(overNumber = index + 1, deliveries = over)
                                }
                            }
                        }
                    }

                }
            }

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text("Save Summary")
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4864F1))
            ) {
                Text("Go Back to Scoring")
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

