package com.example.cricscore.model


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.cricscore.model.data.PlayerStats


class MatchState(
    val totalOvers: Int,
    striker: String,
    nonStriker: String
) {
    val outPlayers = mutableSetOf<String>()
    var runs by mutableIntStateOf(0)
    var wickets by mutableIntStateOf(0)
    var currentOver by mutableIntStateOf(0)
    var currentBall by mutableIntStateOf(0)

    var strikerName by mutableStateOf(striker)
    var strikerRuns by mutableIntStateOf(0)
    var strikerBalls by mutableIntStateOf(0)

    var nonStrikerName by mutableStateOf(nonStriker)
    var nonStrikerRuns by mutableIntStateOf(0)
    var nonStrikerBalls by mutableIntStateOf(0)

    var isStrikerOnStrike by mutableStateOf(true)

    var perBallResults = mutableStateListOf<String>()
    var overBallHistory = mutableStateListOf<List<String>>()

    data class Snapshot(
        val runs: Int,
        val wickets: Int,
        val over: Int,
        val ball: Int,
        val striker: Triple<String, Int, Int>,
        val nonStriker: Triple<String, Int, Int>,
        val isStrikerOnStrike: Boolean,
        val overState: List<String>
    )

    val history = mutableStateListOf<Snapshot>()

    fun saveSnapshot() {
        history.add(
            Snapshot(
                runs, wickets, currentOver, currentBall,
                Triple(strikerName, strikerRuns, strikerBalls),
                Triple(nonStrikerName, nonStrikerRuns, nonStrikerBalls),
                isStrikerOnStrike,
                perBallResults.toList()
            )
        )
    }


    fun undo() {
        if (history.isNotEmpty()) {
            val last = history.removeLast()
            runs = last.runs
            wickets = last.wickets
            currentOver = last.over
            currentBall = last.ball
            strikerName = last.striker.first
            strikerRuns = last.striker.second
            strikerBalls = last.striker.third
            nonStrikerName = last.nonStriker.first
            nonStrikerRuns = last.nonStriker.second
            nonStrikerBalls = last.nonStriker.third
            isStrikerOnStrike = last.isStrikerOnStrike
            perBallResults.clear()
            perBallResults.addAll(last.overState)
        }
    }

    fun incrementBall() {
        val last = perBallResults.lastOrNull() ?: ""
        if (last !in listOf("Wd", "Nb")) {
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

    fun handleWicket(
        outType: String,
        askNewBatsman: ((String) -> Unit) -> Unit
    ) {
        saveSnapshot()
        wickets++
        perBallResults.add(outType)

        if (isStrikerOnStrike) {
            strikerBalls++
            outPlayers.add(strikerName)  // ✅ Track who got out
            askNewBatsman { name ->
                strikerName = name
                strikerRuns = 0
                strikerBalls = 0
                isStrikerOnStrike = true
            }
        } else {
            nonStrikerBalls++
            outPlayers.add(nonStrikerName)  // ✅ Track who got out
            askNewBatsman { name ->
                nonStrikerName = name
                nonStrikerRuns = 0
                nonStrikerBalls = 0
                isStrikerOnStrike = false
            }
        }

        incrementBall()
    }


    fun handleRunOut(run: Int, outName: String, newName: String, strikerNow: Boolean) {
        saveSnapshot()
        wickets++
        runs += run
        perBallResults.add("Run Out")

        outPlayers.add(outName)

        if (outName == strikerName) {
            strikerRuns += run
            strikerBalls++
            strikerName = newName
            strikerRuns = 0
            strikerBalls = 0
        } else {
            nonStrikerRuns += run
            nonStrikerBalls++
            nonStrikerName = newName
            nonStrikerRuns = 0
            nonStrikerBalls = 0
        }

        isStrikerOnStrike = strikerNow
        incrementBall()
    }



    fun getBattingStats(): List<PlayerStats> {
        return listOf(
            PlayerStats(
                name = strikerName,
                runs = strikerRuns,
                balls = strikerBalls,
                isOut = strikerName in outPlayers
            ),
            PlayerStats(
                name = nonStrikerName,
                runs = nonStrikerRuns,
                balls = nonStrikerBalls,
                isOut = nonStrikerName in outPlayers
            )
        )
    }

    fun getOverWiseResults(): List<OverBreakdown> {
        val allOvers = overBallHistory.toMutableList()
        if (perBallResults.isNotEmpty()) {
            allOvers.add(perBallResults.toList())
        }
        return allOvers.mapIndexed { index, balls ->
            val runs = balls.sumOf { it.toIntOrNull() ?: if (it == "4") 4 else if (it == "6") 6 else 0 }
            val wkts = balls.count { it in listOf("W", "Run Out", "Bowled", "Catch Out", "LBW", "Hit Wicket") }
            OverBreakdown(index + 1, runs, wkts, balls)
        }
    }

    fun handleRun(label: String) {
        saveSnapshot()
        when (label) {
            "Wide" -> {
                runs++
                perBallResults.add("Wd")
            }
            "No Ball" -> {
                runs++
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
    fun resetForNextInnings() {
        runs = 0
        wickets = 0
        currentOver = 0
        currentBall = 0

        // Assume striker/non-striker names will be entered manually
        strikerRuns = 0
        strikerBalls = 0
        nonStrikerRuns = 0
        nonStrikerBalls = 0

        isStrikerOnStrike = true

        perBallResults.clear()
        overBallHistory.clear()
        history.clear()
        outPlayers.clear()
    }

}

