package com.example.cricscore.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cricscore.R
import com.example.cricscore.view.components.BattingStatCard
import com.example.cricscore.view.components.NewBatsmanDialog
import com.example.cricscore.view.components.RunOutDialog
import com.example.cricscore.view.components.ScoreSummaryCard
import com.example.cricscore.view.components.WicketDialog
import com.example.cricscore.viewModel.MatchPhase
import com.example.cricscore.viewModel.MatchViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ScoringScreen(
    navController: NavHostController,
    matchViewModel: MatchViewModel = viewModel()
) {

    val totalOvers       = matchViewModel.totalOvers
    val strikerInit      = matchViewModel.strikerName
    val nonStrikerInit   = matchViewModel.nonStrikerName
    val battingTeamLabel = matchViewModel.battingTeam


    matchViewModel.initMatchState(totalOvers, strikerInit, nonStrikerInit)
    val matchState = matchViewModel.matchState!!
    LaunchedEffect(matchState.runs, matchState.wickets, matchState.currentBall) {
        matchViewModel.updateLiveInnings(matchState, matchViewModel.battingTeam)
    }



    var showEndInningsDialog  by remember { mutableStateOf(false) }
    var showMatchResultDialog by remember { mutableStateOf(false) }
    var winningMessage        by remember { mutableStateOf("") }


    var wicketTypeDialog  by remember { mutableStateOf(false) }
    var runOutDialog      by remember { mutableStateOf(false) }
    var newBatsmanDialog  by remember { mutableStateOf(false) }
    var newBatsmanCallback by remember { mutableStateOf<(String) -> Unit>({}) }



    val target        = matchViewModel.targetScore
    val isSecondInnings = matchViewModel.phase == MatchPhase.SECOND
    val ballsLeft       = (totalOvers * 6) -
            (matchState.currentOver * 6 + matchState.currentBall)
    val runsLeft        = target - matchState.runs

    LaunchedEffect(matchState.currentOver, matchState.wickets) {
        val inningsOver = matchState.currentOver == totalOvers || matchState.wickets == matchViewModel.numPlayersPerTeam - 1

        if (inningsOver && matchViewModel.phase != MatchPhase.COMPLETE) {
            showEndInningsDialog = true
        }


        if (isSecondInnings) {
            when {
                matchState.runs >= target -> {
                    winningMessage =
                        "$battingTeamLabel won by ${10 - matchState.wickets} wickets"
                    showMatchResultDialog = true
                    matchViewModel.phase = MatchPhase.COMPLETE
                }

                ballsLeft == 0 || matchState.wickets >= 10 -> {
                    winningMessage =
                        if (matchState.runs < target - 1) {
                            val margin = target - 1 - matchState.runs
                            val winner =
                                if (battingTeamLabel == "Team B") "Team A" else "Team B"
                            "$winner won by $margin runs"
                        } else "Match Drawn"
                    showMatchResultDialog = true
                    matchViewModel.phase = MatchPhase.COMPLETE
                }
            }
        }
    }


    Box(Modifier.fillMaxSize()) {


        Image(
            painter = painterResource(R.drawable.playing_cricket),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.6f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(25.dp))
            Text("Live Scoreboard", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        matchViewModel.updateLiveInnings(matchState, battingTeamLabel)
                        navController.navigate("matchSummary")
                    }
            ) {
                ScoreSummaryCard(
                    teamName   = "$battingTeamLabel Batting",
                    runs       = matchState.runs,
                    wickets    = matchState.wickets,
                    overText   = "${matchState.currentOver}.${matchState.currentBall}",
                    runRate    = if (matchState.currentOver + matchState.currentBall / 6.0 > 0)
                        String.format(
                            "%.2f",
                            matchState.runs / (matchState.currentOver + matchState.currentBall / 6.0)
                        )
                    else "0.00",
                    totalOvers = totalOvers
                )
            }

            if (isSecondInnings) {
                Text(
                    "Target: $target  |  Need $runsLeft from $ballsLeft balls",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("Batting Stats", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            BattingStatCard(
                matchState.strikerName,
                matchState.strikerRuns,
                matchState.strikerBalls,
                matchState.isStrikerOnStrike
            )
            BattingStatCard(
                matchState.nonStrikerName,
                matchState.nonStrikerRuns,
                matchState.nonStrikerBalls,
                !matchState.isStrikerOnStrike
            )

            Spacer(Modifier.height(16.dp))
            Text("This Over", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(6.dp)) {
                matchState.perBallResults.forEach {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF007F0E)),
                        contentAlignment = Alignment.Center
                    ) { Text(it, color = Color.White) }
                }
            }


            Spacer(Modifier.height(16.dp))
            val rows = listOf(
                listOf("Dot", "1", "2"),
                listOf("3", "4", "6"),
                listOf("Wide", "Wicket", "No Ball")
            )
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                    row.forEach { label ->
                        Button(
                            onClick = {
                                if (label == "Wicket") wicketTypeDialog = true
                                else matchState.handleRun(label)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004C3F)),
                            modifier = Modifier.weight(1f).padding(4.dp)
                        ) {
                            Text(label, color = Color.White)
                        }
                    }
                }
            }


            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                Button(
                    onClick = { matchState.undo() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Undo", tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Undo", color = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        matchViewModel.updateLiveInnings(matchState, battingTeamLabel)
                        navController.navigate("matchSummary")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "End", tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("ScoreCard", color = Color.White)
                }
            }

            Spacer(Modifier.height(32.dp))
        }


        if (wicketTypeDialog) {
            WicketDialog(
                onDismiss = { wicketTypeDialog = false },
                onOutTypeSelected = {
                    wicketTypeDialog = false
                    if (it == "Run Out") runOutDialog = true
                    else matchState.handleWicket(it) { cb -> newBatsmanDialog = true; newBatsmanCallback = cb }
                }
            )
        }

        if (runOutDialog) {
            RunOutDialog(
                strikerName = matchState.strikerName,
                nonStrikerName = matchState.nonStrikerName,
                onDismiss = { runOutDialog = false },
                onRunOutConfirmed = { run, out, newName, strikerNow ->
                    runOutDialog = false
                    matchState.handleRunOut(run, out, newName, strikerNow)
                }
            )

        }

        if (newBatsmanDialog) {
            NewBatsmanDialog(
                onDismiss = { newBatsmanDialog = false },
                onConfirm = { newName ->
                    newBatsmanDialog = false
                    newBatsmanCallback(newName)
                }
            )
        }

        if (showEndInningsDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Innings Completed") },
                text  = { Text("What would you like to do next?") },
                confirmButton = {
                    Button(onClick = {
                        matchViewModel.updateLiveInnings(matchState, battingTeamLabel)
                        matchViewModel.phase = MatchPhase.SECOND
                        matchViewModel.targetScore = matchState.runs + 1
                        matchViewModel.battingTeam =
                            if (battingTeamLabel == "Team A") "Team B" else "Team A"
                        showEndInningsDialog = false
                        matchState.resetForNextInnings()
                    }) { Text("Continue to 2nd Innings") }
                },
                dismissButton = {
                    Button(onClick = {
                        matchViewModel.updateLiveInnings(matchState, battingTeamLabel)
                        matchViewModel.phase = MatchPhase.COMPLETE
                        showEndInningsDialog = false
                        navController.navigate("matchSummary")
                    }) { Text("Save Only") }
                }
            )
        }

        if (showMatchResultDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Match Over") },
                text  = { Text(winningMessage) },
                confirmButton = {
                    Button(onClick = {
                        matchViewModel.updateLiveInnings(matchState, battingTeamLabel)
                        matchViewModel.phase = MatchPhase.COMPLETE
                        showMatchResultDialog = false
                        navController.navigate("matchSummary")
                    }) { Text("View Summary") }
                }
            )
        }
    }
}
