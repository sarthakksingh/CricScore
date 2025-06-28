package com.example.cricscore.view.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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


data class MatchStateSnapshot(
    val runs: Int,
    val wickets: Int,
    val currentOver: Int,
    val currentBall: Int,
    val striker: Triple<String, Int, Int>,
    val nonStriker: Triple<String, Int, Int>,
    val isStrikerOnStrike: Boolean,
    val perBallCopy: List<String>
)


@SuppressLint("DefaultLocale")
@Composable
fun ScoringScreen(
    teamName: String = "Team A",
    totalOvers: Int = 5,
    player1: String = "Player 1",
    player2: String = "Player 2"
) {

    var runs by remember { mutableIntStateOf(0) }
    var wickets by remember { mutableIntStateOf(0) }
    var currentOver by remember { mutableIntStateOf(0) }
    var currentBall by remember { mutableIntStateOf(0) }
    var strikerName by remember { mutableStateOf(player1) }
    var nonStrikerName by remember { mutableStateOf(player2) }
    var strikerRuns by remember { mutableIntStateOf(0) }
    var strikerBalls by remember { mutableIntStateOf(0) }
    var nonStrikerRuns by remember { mutableIntStateOf(0) }
    var nonStrikerBalls by remember { mutableIntStateOf(0) }
    var isStrikerOnStrike by remember { mutableStateOf(true) }
    var perBallResults = remember { mutableStateListOf<String>() }
    var overBallHistory = remember { mutableStateListOf<List<String>>() }
    var previousStates = remember { mutableStateListOf<MatchStateSnapshot>() }


    var wicketTypeDialog by remember { mutableStateOf(false) }
    var runOutDialog by remember { mutableStateOf(false) }
    var newBatsmanDialog by remember { mutableStateOf(false) }
    var newBatsmanCallback by remember { mutableStateOf<(String) -> Unit>({}) }


    fun saveState() {
        previousStates.add(
            MatchStateSnapshot(
                runs, wickets, currentOver, currentBall,
                Triple(strikerName, strikerRuns, strikerBalls),
                Triple(nonStrikerName, nonStrikerRuns, nonStrikerBalls),
                isStrikerOnStrike,
                perBallResults.toList()
            )
        )
    }

    fun undoLastAction() {
        if (previousStates.isNotEmpty()) {
            val last = previousStates.removeAt(previousStates.lastIndex)
            runs = last.runs
            wickets = last.wickets
            currentOver = last.currentOver
            currentBall = last.currentBall
            strikerName = last.striker.first
            strikerRuns = last.striker.second
            strikerBalls = last.striker.third
            nonStrikerName = last.nonStriker.first
            nonStrikerRuns = last.nonStriker.second
            nonStrikerBalls = last.nonStriker.third
            isStrikerOnStrike = last.isStrikerOnStrike
            perBallResults.clear()
            perBallResults.addAll(last.perBallCopy)
        }
    }


    fun incrementBall() {
        val last = perBallResults.lastOrNull() ?: ""
        val isLegal = last !in listOf("Wd", "Nb")
        if (isLegal) {
            currentBall++
            if (currentBall == 6) {
                currentOver++
                currentBall = 0
                overBallHistory.add(perBallResults.toList())
                perBallResults.clear()
                isStrikerOnStrike = !isStrikerOnStrike
            }
        }
    }

    fun askNewBatsman(callback: (String) -> Unit) {
        newBatsmanCallback = callback
        newBatsmanDialog = true
    }

    fun handleWicket(outType: String) {
        wickets += 1
        perBallResults.add(outType)
        if (isStrikerOnStrike) {
            strikerBalls++
            askNewBatsman { newName ->
                strikerName = newName
                strikerRuns = 0
                strikerBalls = 0
                isStrikerOnStrike = true
            }
        } else {
            nonStrikerBalls++
            askNewBatsman { newName ->
                nonStrikerName = newName
                nonStrikerRuns = 0
                nonStrikerBalls = 0
                isStrikerOnStrike = false
            }
        }
        incrementBall()
    }


    fun handleRunOut(
        run: Int,
        outPlayer: String,
        newBatsman: String,
        strikerNow: Boolean
    ) {
        wickets += 1
        runs += run
        perBallResults.add("Run Out")

        if (outPlayer == strikerName) {
            strikerRuns += run
            strikerBalls++
            strikerName = newBatsman
            strikerRuns = 0
            strikerBalls = 0
        } else {
            nonStrikerRuns += run
            nonStrikerBalls++
            nonStrikerName = newBatsman
            nonStrikerRuns = 0
            nonStrikerBalls = 0
        }

        isStrikerOnStrike = strikerNow
        incrementBall()
    }

    fun updateRuns(label: String) {
        saveState()
        when (label) {
            "Wicket" -> wicketTypeDialog = true
            "Wide" -> {
                runs += 1
                perBallResults.add("Wd")
            }
            "No Ball" -> {
                runs += 1
                perBallResults.add("Nb")
            }
            else -> {
                val run = label.toIntOrNull() ?: 0
                runs += run
                if (isStrikerOnStrike) {
                    strikerRuns += run
                    strikerBalls++
                } else {
                    nonStrikerRuns += run
                    nonStrikerBalls++
                }
                perBallResults.add(label)
                if (run % 2 != 0) isStrikerOnStrike = !isStrikerOnStrike
                incrementBall()
            }
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.playing_cricket),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.6f)
        )
        Text(
            text = "Live ScoreBoard",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 26.dp, top = 30.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            ScoreSummaryCard(
                teamName = "$teamName Batting",
                runs = runs,
                wickets = wickets,
                overText = "${currentOver}.${currentBall}",
                runRate = if (currentOver + currentBall / 6.0 > 0) String.format("%.2f", runs / (currentOver + currentBall / 6.0)) else "0.00",
                totalOvers = totalOvers
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Batting Stats", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            BattingStatCard(name = strikerName, runs = strikerRuns, balls = strikerBalls, isOnStrike = isStrikerOnStrike)
            BattingStatCard(name = nonStrikerName, runs = nonStrikerRuns, balls = nonStrikerBalls, isOnStrike = !isStrikerOnStrike)
            Spacer(modifier = Modifier.height(16.dp))
            Text("This Over", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                perBallResults.forEach { label ->
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
                listOf("0", "1", "2"),
                listOf("3", "4", "6"),
                listOf("Wide", "Wicket", "No Ball")
            )
            inputButtons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { label ->
                        Button(
                            onClick = { updateRuns(label) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
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
                    onClick = { undoLastAction() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Undo", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Undo", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* End Inning Logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "End", tint = Color.White)
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
                        handleWicket(it)
                    }
                }
            )
        }
        if (runOutDialog) {
            RunOutDialog(
                onDismiss = { runOutDialog = false },
                onRunOutConfirmed = { run, out, newName, strikerNow ->
                    runOutDialog = false
                    handleRunOut(run, out, newName, strikerNow)
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


@Composable
fun WicketDialog(
    onDismiss: () -> Unit,
    onOutTypeSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select type of Wicket") },
        text = {
            Column {
                listOf("Catch Out", "Bowled", "Run Out", "LBW", "Hit Wicket").forEach { type ->
                    TextButton(onClick = { onOutTypeSelected(type) }) {
                        Text(type)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun RunOutDialog(
    onDismiss: () -> Unit,
    onRunOutConfirmed: (run: Int, outName: String, newName: String, strikerNow: Boolean) -> Unit
) {
    var runTaken by remember { mutableStateOf("") }
    var whoOut by remember { mutableStateOf("") }
    var newBatsman by remember { mutableStateOf("") }
    var strikeNow by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Run Out Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = runTaken,
                    onValueChange = { runTaken = it },
                    label = { Text("Runs Taken (0,1,2)") }
                )
                OutlinedTextField(
                    value = whoOut,
                    onValueChange = { whoOut = it },
                    label = { Text("Who got out?") }
                )
                OutlinedTextField(
                    value = newBatsman,
                    onValueChange = { newBatsman = it },
                    label = { Text("New Batsman Name") }
                )
                Row {
                    Text("Who is on Strike?")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = strikeNow,
                        onClick = { strikeNow = true }
                    )
                    Text("Striker")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = !strikeNow,
                        onClick = { strikeNow = false }
                    )
                    Text("Non-Striker")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val run = runTaken.toIntOrNull() ?: 0
                onRunOutConfirmed(run, whoOut, newBatsman, strikeNow)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun NewBatsmanDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var batsmanName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Batsman") },
        text = {
            OutlinedTextField(
                value = batsmanName,
                onValueChange = { batsmanName = it },
                label = { Text("Enter new batsman name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(batsmanName) }) { Text("Confirm") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@Composable
fun ScoreSummaryCard(
    teamName: String,
    runs: Int,
    wickets: Int,
    overText: String,
    runRate: String,
    totalOvers: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFFFFFFF).copy(alpha = 0.65f))
    ) {

        Text(
            text = teamName,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 12.dp)
        )


        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 38.dp)
        ) {
            Text(
                text = "$runs/$wickets",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )


            Text(
                text = "RR: $runRate",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,

                )
        }
        Text(
            text = "$overText/$totalOvers",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 12.dp)
        )
    }
}


@Composable
fun BattingStatCard(
    name: String,
    runs: Int,
    balls: Int,
    isOnStrike: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isOnStrike) Color(0xFF007F0E) else Color(0xFF004C3F))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("$runs ($balls)", color = Color.White)
            if (isOnStrike) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("*", color = Color.Yellow, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScoringScreenPreview() {
    MaterialTheme {
        ScoringScreen()
    }
}