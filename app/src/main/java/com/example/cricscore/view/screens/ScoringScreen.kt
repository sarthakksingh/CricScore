package com.example.cricscore.view.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cricscore.R
import com.example.cricscore.model.MatchState
import com.example.cricscore.view.components.BattingStatCard
import com.example.cricscore.view.components.NewBatsmanDialog
import com.example.cricscore.view.components.RunOutDialog
import com.example.cricscore.view.components.ScoreSummaryCard
import com.example.cricscore.view.components.WicketDialog


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@SuppressLint("DefaultLocale")
@Composable
fun ScoringScreen(
    teamName: String = "Team A",
    totalOvers: Int = 5,
    player1: String = "Player 1",
    player2: String = "Player 2"
) {
    val matchState = remember { MatchState(totalOvers, player1, player2) }

    var wicketTypeDialog by remember { mutableStateOf(false) }
    var runOutDialog by remember { mutableStateOf(false) }
    var newBatsmanDialog by remember { mutableStateOf(false) }
    var newBatsmanCallback by remember { mutableStateOf<(String) -> Unit>({}) }

    fun askNewBatsman(callback: (String) -> Unit) {
        newBatsmanCallback = callback
        newBatsmanDialog = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.playing_cricket),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.6f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Live ScoreBoard",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))


            ScoreSummaryCard(
                teamName = "$teamName Batting",
                runs = matchState.runs,
                wickets = matchState.wickets,
                overText = "${matchState.currentOver}.${matchState.currentBall}",
                runRate = if (matchState.currentOver + matchState.currentBall / 6.0 > 0)
                    String.format("%.2f", matchState.runs / (matchState.currentOver + matchState.currentBall / 6.0))
                else "0.00",
                totalOvers = totalOvers
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Batting Stats", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            BattingStatCard(
                name = matchState.strikerName,
                runs = matchState.strikerRuns,
                balls = matchState.strikerBalls,
                isOnStrike = matchState.isStrikerOnStrike
            )
            BattingStatCard(
                name = matchState.nonStrikerName,
                runs = matchState.nonStrikerRuns,
                balls = matchState.nonStrikerBalls,
                isOnStrike = !matchState.isStrikerOnStrike
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("This Over", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                matchState.perBallResults.forEach { label ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF007F0E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = label, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Select Ball Result", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            val inputButtons = listOf(
                listOf("Dot", "1", "2"),
                listOf("3", "4", "6"),
                listOf("Wide", "Wicket", "No Ball")
            )
            inputButtons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { label ->
                        Button(
                            onClick = {
                                if (label == "Wicket") {
                                    wicketTypeDialog = true
                                } else {
                                    matchState.handleRun(label)
                                }
                            },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004C3F))
                        ) {
                            Text(text = label, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { matchState.undo() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Undo", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Undo", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* End Inning Logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "End", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("End Inning", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (wicketTypeDialog) {
            WicketDialog(
                onDismiss = { wicketTypeDialog = false },
                onOutTypeSelected = {
                    wicketTypeDialog = false
                    if (it == "Run Out") {
                        runOutDialog = true
                    } else {
                        matchState.handleWicket(it) { newBatsmanName ->
                            askNewBatsman(newBatsmanName)
                        }

                    }
                }
            )
        }

        if (runOutDialog) {
            RunOutDialog(
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
                onConfirm = {
                    newBatsmanDialog = false
                    newBatsmanCallback(it)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScoringScreenPreview() {
    MaterialTheme {
        ScoringScreen()
    }
}